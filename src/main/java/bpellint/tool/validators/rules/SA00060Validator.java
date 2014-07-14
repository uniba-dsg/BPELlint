package bpellint.tool.validators.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import bpellint.model.ComparableNode;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Referable;
import bpellint.model.Standards;
import bpellint.model.bpel.OptionalElementNotPresentException;
import bpellint.model.bpel.flow.FlowElement;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.bpel.mex.MessageActivityImpl;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.model.bpel.mex.ReplyElement;
import bpellint.model.bpel.mex.MessageActivity.Type;
import bpellint.model.wsdl.OperationElement;
import bpellint.tool.validators.result.ValidationCollector;

import com.google.common.collect.Sets;

import nu.xom.Node;
import nu.xom.Nodes;


public class SA00060Validator extends Validator {

	private class OperationMembers {
		private List<MessageActivity> onEvents = new LinkedList<>();
		private List<MessageActivity> onMessages = new LinkedList<>();
		private List<MessageActivity> receives = new LinkedList<>();
		private List<MessageActivity> replies = new LinkedList<>();
		private TreeSet<ComparableNode> dom = new TreeSet<>();

		void add(MessageActivity messageActivity) {
			if (messageActivity.getType() == Type.ON_EVENT) {
				onEvents.add(messageActivity);
				validate(new OnEventElement(messageActivity.toXOM(), processContainer));
			}
			if (messageActivity.getType() == Type.ON_MESSAGE) {
				onMessages.add(messageActivity);
			}
			if (messageActivity.getType() == Type.RECEIVE) {
				receives.add(messageActivity);
			}
			if (messageActivity.getType() == Type.REPLY) {
				replies.add(messageActivity);
			}
			dom.add(new ComparableNode(messageActivity));
			listAncestors(messageActivity);
		}

		private void listAncestors(Referable startActivity) {
			NodeHelper node = new NodeHelper(startActivity);
			while (!"process".equals(node.getParent().getLocalName())) {
				node = node.getParent();
				dom.add(new ComparableNode(node));
			}
		}

		boolean isSimple() {
			return onEvents.size() + onMessages.size() + receives.size() <= 1;
		}

		boolean areMarkedForSimultaneousOnEvent() {
            return onEvents.isEmpty() || haveAllReplies(onMessages) && haveAllReplies(receives);
        }

		void areMarkedForSimultaneousRequestResponse() {
			if (receives.size() + onMessages.size() <= 1) {
				return;
			}
			checkAllMarkedUp(receives);
			checkAllMarkedUp(onMessages);
		}

		private void checkAllMarkedUp(List<MessageActivity> messageExchange) {
			for (Referable messageActivity : messageExchange) {
				Set<ComparableNode> receivingActivityTailSet = dom.tailSet(new ComparableNode(messageActivity));

				Set<ComparableNode> intersection = intersectionRequestResponse(receivingActivityTailSet);
				checkForSimultaneousActivities(intersection);
			}
		}

		private Set<ComparableNode> intersectionRequestResponse(Set<ComparableNode> tailSet) {
			Set<ComparableNode> intersection = new HashSet<>();
			for (Referable reply : replies) {
				Set<ComparableNode> replyHeadSet = dom.headSet(new ComparableNode(reply));
				Set<ComparableNode> replyIntersection = Sets.intersection(tailSet, replyHeadSet);
				if (!replyIntersection.isEmpty()
						&& (intersection.size() > replyIntersection.size() || intersection.isEmpty())) {
					intersection = replyIntersection;
				}
			}
			return intersection;
		}

		private void checkForSimultaneousActivities(Set<ComparableNode> intersection) {
			if (intersection.isEmpty()) {
				return;
			}
			Set<ComparableNode> intermediaryReceives = Sets.intersection(intersection, convertForComparison(receives));
			Set<ComparableNode> intermediaryOnMessage = Sets.intersection(intersection, convertForComparison(onMessages));
			if (!(intermediaryReceives.size() <= 1 && intermediaryOnMessage.size() <= 1)) {
				checkSimultaneousMarkUp(intermediaryReceives);
				checkSimultaneousMarkUp(intermediaryOnMessage);
			}
		}

		private void checkSimultaneousMarkUp(Set<ComparableNode> messageActivities) {
			for (ComparableNode comparableNode : messageActivities) {
				MessageActivityImpl messageActivity = new MessageActivityImpl(comparableNode, processContainer);
				if ("".equals(messageActivity.getMessageExchangeAttribute())) {
					addViolation(comparableNode);
				}
			}
		}

		private boolean haveAllReplies(List<MessageActivity> messageActivities) {
			for (MessageActivity messageActivity : messageActivities) {
				if (!hasOnEventInScope(messageActivity)) {
					return true;
				}
				if (!hasReply(messageActivity)) {
					addViolation(messageActivity);
					return false;
				}
			}
			return true;
		}

		private boolean hasOnEventInScope(MessageActivity messageActivity) {
			Set<ComparableNode> headSet = dom.headSet(new ComparableNode(messageActivity));
			return Sets.intersection(headSet, convertForComparison(onEvents)).size() > 0;
		}

		private boolean hasReply(MessageActivity messageActivity) {
			for (MessageActivity reply : replies) {
				if (isMarked(messageActivity, reply)) {
					return true;
				}
			}
			return false;
		}

		private Set<ComparableNode> convertForComparison(List<MessageActivity> messageActivities) {
			/*
			 * TODO this could be part of ComparableNode as static method
			 */
			Set<ComparableNode> comparableNodes = new HashSet<>();
			for (Referable referable : messageActivities) {
				comparableNodes.add(new ComparableNode(referable));
			}
			return comparableNodes;
		}

	}

	public SA00060Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Map<ComparableNode, OperationMembers> operationMemberMap = getAllRequestResponseMessageExchanges();
		for (ComparableNode operation : operationMemberMap.keySet()) {
			OperationMembers operationMembers = operationMemberMap.get(operation);
			if (operationMembers.isSimple()) {
				continue;
			}
			operationMembers.areMarkedForSimultaneousOnEvent();
			operationMembers.areMarkedForSimultaneousRequestResponse();
		}
		warnInLinkedFlows();
	}

	private void warnInLinkedFlows() {
		for (FlowElement flow : processContainer.getAllFlows()) {
				try {
					if (flow.getLinkElements().isEmpty()) {
						continue;
					}
				} catch (OptionalElementNotPresentException e) {
					// no links are present
					continue;
				}
				Nodes messageActivities = flow.toXOM().query(".//*[@operation]", Standards.CONTEXT);
				if (messageActivities.isEmpty()) {
					continue;
				}
				addWarning(flow, "<flow> may contain properly linked message activities that are marked as SA00060 violation " +
						"even though they cannot be executed in parallel.");
		}
	}

	private Map<ComparableNode, OperationMembers> getAllRequestResponseMessageExchanges() {
		Nodes operationalNodes = processContainer.getProcess().toXOM()
				.query("//bpel:*[@operation]", Standards.CONTEXT);
		Map<ComparableNode, OperationMembers> operations = new HashMap<>(operationalNodes.size() / 2 + 1);
		for (Node node : operationalNodes) {
			try {
				MessageActivityImpl messageActivity = new MessageActivityImpl(new NodeHelper(node), processContainer);
				OperationElement operationElement = messageActivity.getOperation();
				if (!operationElement.isRequestResponse()) {
					continue;
				}
				ComparableNode operation = new ComparableNode(operationElement);
				if (operations.get(operation) == null) {
					operations.put(operation, new OperationMembers());
				}
				operations.get(operation).add(messageActivity);
			} catch (NavigationException e) {
				// ignore operations that cannot be resolved
			}
		}
		return operations;
	}

	private void validate(OnEventElement onEvent) {
		String correspondingReply = ".//bpel:reply[@partnerLink='"
				+ onEvent.getPartnerLinkAttribute() + "']" + "[@operation='"
				+ onEvent.getOperationAttribute() + "']";
		Nodes replies = onEvent.toXOM().query(correspondingReply, Standards.CONTEXT);
		if (isOperationUsedUniquelyInOnEventEnclosingScope(onEvent, replies.size())) {
			// ignore if there are no replies, this is checked elsewhere
			return;
		}
		if (replies.size() == 1 && isMarked(onEvent, replies.get(0))) {
			return;
		}
		addViolation(onEvent);
	}

	private boolean isOperationUsedUniquelyInOnEventEnclosingScope(OnEventElement onEvent,
			int repliesInOnEvent) {
		String allOperationMembersInScope = ".//bpel:*[@partnerLink='"
				+ onEvent.getPartnerLinkAttribute() + "']" + "[@operation='"
				+ onEvent.getOperationAttribute() + "']";
		return onEvent.getEnclosingScope().toXOM()
				.query(allOperationMembersInScope, Standards.CONTEXT).size() == 1 + repliesInOnEvent;
	}

	private boolean isMarked(OnEventElement onEvent, Node node) {
		return isMarked(onEvent, new ReplyElement(node, processContainer));
	}

	private boolean isMarked(MessageActivity messageActivity, MessageActivity reply) {
		return reply.getMessageExchangeAttribute().equals(messageActivity.getMessageExchangeAttribute())
				&& !messageActivity.getMessageExchangeAttribute().isEmpty();
	}

	@Override
	public int getSaNumber() {
		return 60;
	}

}
