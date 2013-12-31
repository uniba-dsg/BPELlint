package isabel.tool.validators.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Node;
import nu.xom.Nodes;

import isabel.model.ComparableNode;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.bpel.mex.OnEventElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.tool.impl.ValidationCollector;

public class SA00060Validator extends Validator {

	public SA00060Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<OnEventElement> onEvents = fileHandler.getAllOnEvents();
		for (OnEventElement onEvent : onEvents) {
			try {
				validate(onEvent);
			} catch (NavigationException e) {
				// ignore element without operation
			}
		}
		Map<MessageActivity, Boolean> imas = new HashMap<>();
		imas.putAll(getImas(fileHandler.getAllReceives()));
		imas.putAll(getImas(fileHandler.getAllOnMessages()));
		for (MessageActivity inboundMessageActivity : imas.keySet()) {
			Boolean isClosed = !imas.get(inboundMessageActivity);
			if (isClosed) {
				continue;
			}
			for (MessageActivity similarIma : imas.keySet()) {
				if (isSimilarOperation(inboundMessageActivity, similarIma)) {
					Nodes replies = new NodeHelper(inboundMessageActivity.toXOM()).getEnclosingScope().toXOM().query(
							".//bpel:reply[@partnerLink='"
									+ inboundMessageActivity.getPartnerLinkAttribute()
									+ "'][@operation='"
									+ inboundMessageActivity.getOperationAttribute() + "']",
							Standards.CONTEXT);
				}
			}
		}
	}

	private boolean isSimilarOperation(MessageActivity ima, MessageActivity similarIma) {
		boolean sameActivityIsNotSimilar = new ComparableNode(ima.toXOM())
				.equals(new ComparableNode(similarIma.toXOM()));
		if (sameActivityIsNotSimilar) {
			return false;
		}
		try {
			boolean samePortType = new ComparableNode(ima.getPortType().toXOM())
					.equals(new ComparableNode(similarIma.getPortType().toXOM()));
			boolean sameOperation = new ComparableNode(ima.getOperation().toXOM())
					.equals(new ComparableNode(similarIma.getOperation().toXOM()));
			return samePortType && sameOperation;
		} catch (NavigationException e) {
			return false;
		}
	}

	private <E extends MessageActivity> Map<MessageActivity, Boolean> getImas(
			List<E> messageActivities) {
		Map<MessageActivity, Boolean> imas = new HashMap<>();
		for (MessageActivity messageActivity : messageActivities) {
			try {
				if (messageActivity.getOperation().isRequestResponse()) {
					imas.put(messageActivity, true);
				}
			} catch (NavigationException e) {
				// ignore element without operation
			}
		}
		return imas;
	}

	private void validate(OnEventElement onEvent) throws NavigationException {
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
		String messageExchangeKey = onEvent.getMessageExchangeAttribute();
		return new ReplyElement(node, fileHandler).getMessageExchangeAttribute().equals(
				messageExchangeKey)
				&& !messageExchangeKey.isEmpty();
	}

	@Override
	public int getSaNumber() {
		return 60;
	}

}
