package isabel.tool.validators.rules;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import nu.xom.Node;
import nu.xom.Nodes;

import isabel.model.ComparableNode;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.bpel.mex.MessageActivity.Type;
import isabel.model.bpel.mex.MessageActivityImpl;
import isabel.model.bpel.mex.OnEventElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;

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
				validate(new OnEventElement(messageActivity.toXOM(), fileHandler));
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
			/*
			 * TODO copied from SA00056. DOM may be extractable in extra class
			 */
			NodeHelper node = new NodeHelper(startActivity.toXOM());
			while (!"process".equals(node.getParent().getLocalName())) {
				node = node.getParent();
				dom.add(new ComparableNode(node.toXOM()));
			}
		}

		boolean isSimple() {
			return onEvents.size() + onMessages.size() + receives.size() <= 1;
		}

		boolean areMarkedForSimultaniousOnEvent() {
			if (onEvents.isEmpty()) {
				return true;
			}
			return haveAllReplies(onMessages) && haveAllReplies(receives);
		}

		void areMarkedForSimultaniousRequestResponse() {
			if (receives.size() + onMessages.size() <= 1) {
				return;
			}
			checkAllMarkedUp(receives);
			checkAllMarkedUp(onMessages);
		}

		private void checkAllMarkedUp(List<MessageActivity> messageExchange) {
			for (Referable messageActivity : messageExchange) {
				Set<ComparableNode> receivingActivityTailSet = dom.tailSet(new ComparableNode(
						messageActivity.toXOM()));

				Set<ComparableNode> intersection = intersectionnRequestResponse(receivingActivityTailSet);
				checkForSimultaniousActivities(intersection);
			}
		}

		private Set<ComparableNode> intersectionnRequestResponse(Set<ComparableNode> tailSet) {
			Set<ComparableNode> intersection = new HashSet<>();
			for (Referable reply : replies) {
				Set<ComparableNode> replyHeadSet = dom.headSet(new ComparableNode(reply));
				Set<ComparableNode> replyIntersection = Sets.intersection(tailSet, replyHeadSet);
				if (!replyIntersection.isEmpty()
						&& (intersection.size() > replyIntersection.size() || intersection
								.isEmpty())) {
					intersection = replyIntersection;
				}
			}
			return intersection;
		}

		private void checkForSimultaniousActivities(Set<ComparableNode> intersection) {
			if (intersection.isEmpty()) {
				return;
			}
			Set<ComparableNode> intermidiaryReceives = Sets.intersection(intersection,
					convertForComparission(receives));
			Set<ComparableNode> intermidiaryOnMessage = Sets.intersection(intersection,
					convertForComparission(onMessages));
			if (!(intermidiaryReceives.size() <= 1 && intermidiaryOnMessage.size() <= 1)) {
				checkSimultaniousMarkUp(intermidiaryReceives);
				checkSimultaniousMarkUp(intermidiaryOnMessage);
			}
		}

		private void checkSimultaniousMarkUp(Set<ComparableNode> messageActivities) {
			for (ComparableNode comparableNode : messageActivities) {
				MessageActivityImpl messageActivity = new MessageActivityImpl(comparableNode, fileHandler);
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
			Set<ComparableNode> headSet = dom.headSet(new ComparableNode(messageActivity.toXOM()));
			return Sets.intersection(headSet, convertForComparission(onEvents)).size() > 0;
		}

		private boolean hasReply(MessageActivity messageActivity) {
			for (MessageActivity reply : replies) {
				if (isMarked(messageActivity, reply)) {
					return true;
				}
			}
			return false;
		}

		private Set<ComparableNode> convertForComparission(List<MessageActivity> messageActivities) {
			/*
			 * TODO this could be part of ComparableNode as static method
			 */
			Set<ComparableNode> comparableNodes = new HashSet<>();
			for (Referable referable : messageActivities) {
				comparableNodes.add(new ComparableNode(referable.toXOM()));
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
			operationMembers.areMarkedForSimultaniousOnEvent();
			operationMembers.areMarkedForSimultaniousRequestResponse();
		}
	}

	private Map<ComparableNode, OperationMembers> getAllRequestResponseMessageExchanges() {
		Nodes operationalNodes = fileHandler.getProcess().toXOM()
				.query("//bpel:*[@operation]", Standards.CONTEXT);
		Map<ComparableNode, OperationMembers> operations = new HashMap<>(
				operationalNodes.size() / 2 + 1);
		for (Node node : operationalNodes) {
			try {
				MessageActivityImpl messageActivity = new MessageActivityImpl(new NodeHelper(node), fileHandler);
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
		return isMarked(onEvent, new ReplyElement(node, fileHandler));
	}

	private boolean isMarked(MessageActivity messageActivity, MessageActivity reply) {
		return reply.getMessageExchangeAttribute().equals(
				messageActivity.getMessageExchangeAttribute())
				&& !messageActivity.getMessageExchangeAttribute().isEmpty();
	}

	@Override
	public int getSaNumber() {
		return 60;
	}

}
