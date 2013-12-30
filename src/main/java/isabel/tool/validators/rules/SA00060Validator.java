package isabel.tool.validators.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nu.xom.Node;
import nu.xom.Nodes;

import isabel.model.NavigationException;
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
		Map<MessageActivity, Boolean> imas = new HashMap<>();
		imas.putAll(getImas(fileHandler.getAllOnEvents()));
		imas.putAll(getImas(fileHandler.getAllReceives()));
		imas.putAll(getImas(fileHandler.getAllOnMessages()));
	}

	private <E extends MessageActivity> Map<MessageActivity, Boolean> getImas(
			List<E> messageActivities) {
		Map<MessageActivity, Boolean> imas = new HashMap<>();
		for (MessageActivity messageActivity : messageActivities) {
			try {
				if (messageActivity.getOperation().isRequestResponse()) {
					boolean isOpen = true;
					if (messageActivity instanceof OnEventElement) {
						isOpen = false;
						validate((OnEventElement) messageActivity);
					}
					imas.put(messageActivity, isOpen);
				}
			} catch (NavigationException e) {
				// ignore element without operation
			}
		}
		return imas;
	}

	private void validate(OnEventElement onEvent) throws NavigationException {
		String correspondingReply = ".//bpel:reply[@portType='" + onEvent.getPortTypeAttribute()
				+ "']" + "[@operation='" + onEvent.getOperationAttribute() + "']";
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

	private boolean isMarked(OnEventElement onEvent, Node node) {
		String messageExchangeKey = onEvent.getMessageExchangeAttribute();
		return new ReplyElement(node, fileHandler).getMessageExchangeAttribute()
						.equals(messageExchangeKey) && !messageExchangeKey.isEmpty();
	}

	private boolean isOperationUsedUniquelyInOnEventEnclosingScope(OnEventElement onEvent,
			int repliesInOnEvent) {
		String allOperationMembersInScope = ".//bpel:*[@portType='"
				+ onEvent.getPortTypeAttribute() + "']" + "[@operation='"
				+ onEvent.getOperationAttribute() + "']";
		return onEvent.getEnclosingScope().toXOM().query(allOperationMembersInScope, Standards.CONTEXT).size() == 1 + repliesInOnEvent;
	}

	@Override
	public int getSaNumber() {
		return 60;
	}

}
