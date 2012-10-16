package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00047Validator extends Validator {

	public SA00047Validator(BpelProcessFiles files,
	                        ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		List<Nodes> messageActivityList = getMessageActivities();

		for (Nodes messageActivityNodes : messageActivityList) {
			for (int i = 0; i < messageActivityNodes.size(); i++) {
				Node messageActivity = messageActivityNodes.get(i);
				try {
					if (hasMessagePart(messageActivity)) {
						validateMessagePartConstraint(messageActivity);
					} else {
						validateNoMessagePartConstraint(messageActivity);
					}
				} catch (NavigationException e) {
					// This node could not be validated
				}
			}
		}
	}

	private void validateMessagePartConstraint(Node messageActivity)
			throws NavigationException {
		if (isReceiveOnMessageOnEvent(messageActivity)
				&& !hasVariableOrFromPart(messageActivity)) {
			reportViolation(messageActivity, 2);
		} else if (isReply(messageActivity)
				&& !hasVariableOrToPart(messageActivity)) {
			reportViolation(messageActivity, 3);
		} else if (isInvoke(messageActivity)) {
			validateInvokeMessagePartConstraint(messageActivity);
		}
	}

	private void validateInvokeMessagePartConstraint(Node messageActivity)
			throws NavigationException {
		Node operation = navigator.correspondingOperation(messageActivity);
		if (new OperationHelper(operation).isOneWay()
				&& !hasInputVariableOrToPart(messageActivity)) {
			reportViolation(messageActivity, 4);
		} else if (new OperationHelper(operation).isRequestResponse()
				&& (!hasInputVariableOrToPart(messageActivity) || !hasOutputVariableOrFromPart(messageActivity))) {
			reportViolation(messageActivity, 5);
		}
	}

	private void validateNoMessagePartConstraint(Node messageActivity) {
		if (hasToPartOrFromPart(messageActivity))
			reportViolation(messageActivity, 1);
	}

	private boolean hasMessagePart(Node messageActivity)
			throws NavigationException {
		Node operation = navigator.correspondingOperation(messageActivity);
		if (operation == null)
			throw new NavigationException(
					"WARNING: could not found an associated operation "
							+ getBpelFileName());

		Map<String, Node> messages = navigator.getOperationMessages(
				fileHandler.getAllWsdls(), operation);
		if (messages == null || messages.size() < 1)
			throw new NavigationException(
					"WARNING: could not found an associated message in "
							+ getBpelFileName());

		if (isInvoke(messageActivity)
				|| isReceiveOnMessageOnEvent(messageActivity)) {
			return messages.get("input").query("child::*").size() > 0;
		} else if (isReply(messageActivity)) {
			return messages.get("output").query("child::*").size() > 0;
		} else {
			throw new NavigationException("WARNING: not a message activity..."
					+ getBpelFileName());
		}

	}

	private boolean isInvoke(Node messageActivity) {
		return new NodeHelper(messageActivity).hasLocalName("invoke");
	}

	private boolean isReply(Node messageActivity) {
		return new NodeHelper(messageActivity).hasLocalName("reply");
	}

	private boolean isReceiveOnMessageOnEvent(Node messageActivity) {
		NodeHelper nodeHelper = new NodeHelper(messageActivity);
		return nodeHelper.hasLocalName("receive") || nodeHelper.hasLocalName("onMessage")
				|| nodeHelper.hasLocalName("onEvent");
	}

	private List<Nodes> getMessageActivities() {
		List<Nodes> messageActivities = new ArrayList<>();
		Nodes invokeNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);
		Nodes replyNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:reply", CONTEXT);
		Nodes receiveNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:receive", CONTEXT);
		Nodes onMessageNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:onMessage", CONTEXT);
		Nodes onEventNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:onEvent", CONTEXT);

		messageActivities.add(invokeNodes);
		messageActivities.add(replyNodes);
		messageActivities.add(receiveNodes);
		messageActivities.add(onMessageNodes);
		messageActivities.add(onEventNodes);

		return messageActivities;
	}

	private void reportViolation(Node node, int type) {
		addViolation(getBpelFileName(), node, type);
	}

	private boolean hasToPartOrFromPart(Node messageActivity) {
		return (hasFromPart(messageActivity) || hasToPart(messageActivity));
	}

	private boolean hasToPart(Node messageActivity) {
		Nodes toPart = messageActivity.query("child::bpel:toParts", CONTEXT);
		return toPart.size() > 0;
	}

	private boolean hasFromPart(Node msgActivity) {
		Nodes fromPart = msgActivity.query("child::bpel:fromParts", CONTEXT);
		return fromPart.size() > 0;
	}

	private boolean hasVariable(Node msgActivity) {
		Nodes variable = msgActivity.query("attribute::variable", CONTEXT);
		return variable.size() > 0;
	}

	private boolean hasVariableOrFromPart(Node messageActivity) {
		return (hasVariable(messageActivity) || hasFromPart(messageActivity));
	}

	private boolean hasVariableOrToPart(Node messageActivity) {
		return (hasVariable(messageActivity) || hasToPart(messageActivity));
	}

	private boolean hasInputVariableOrToPart(Node messageActivity) {
		return (navigator.hasInputVariable(messageActivity) || hasToPart(messageActivity));
	}

	private boolean hasOutputVariableOrFromPart(Node messageActivity) {
		return (navigator.hasOutputVariable(messageActivity) || hasFromPart(messageActivity));
	}

	@Override
	public int getSaNumber() {
		return 47;
	}
}
