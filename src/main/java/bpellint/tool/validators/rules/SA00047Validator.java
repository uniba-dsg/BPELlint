package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.wsdl.OperationElement;
import bpellint.tool.validators.result.ValidationCollector;
import nu.xom.Nodes;

import static bpellint.model.Standards.CONTEXT;

public class SA00047Validator extends Validator {

	private static final int TO_PARTS_OR_FROM_PARTS_NOT_ALLOWED = 1;
	private static final int VARIABLE_ATTRIBUTE_OR_FROM_PARTS_MISSING = 2;
	private static final int VARIABLE_ATTRIBUTE_OR_TO_PARTS_MISSING = 3;
	private static final int INPUT_VARIABLE_OR_TO_PARTS_FOR_INVOKE_MISSING = 4;
	private static final int INPUT_VARIABLE_OR_TO_PARTS_AND_OUTPUT_VARIABLE_OR_FROM_PART_FOR_INVOKE_MISSING = 5;

	public SA00047Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {

        for (MessageActivity messageActivity : processContainer.getMessageActivities()) {
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

    private void validateMessagePartConstraint(MessageActivity messageActivity)
            throws NavigationException {
        if (messageActivity.isReceiving()
                && !hasVariableOrFromPart(messageActivity)) {
            addViolation(messageActivity, VARIABLE_ATTRIBUTE_OR_FROM_PARTS_MISSING);
        } else if (isReply(messageActivity)
                && !hasVariableOrToPart(messageActivity)) {
            addViolation(messageActivity, VARIABLE_ATTRIBUTE_OR_TO_PARTS_MISSING);
        } else if (isInvoke(messageActivity)) {
            validateInvokeMessagePartConstraint(messageActivity);
        }
    }

    private void validateInvokeMessagePartConstraint(MessageActivity messageActivity)
            throws NavigationException {
        OperationElement operation = messageActivity.getOperation();
        if (operation.isOneWay()
                && !hasInputVariableOrToPart(messageActivity)) {
            addViolation(messageActivity, INPUT_VARIABLE_OR_TO_PARTS_FOR_INVOKE_MISSING);
        } else if (operation.isRequestResponse()
                && (!hasInputVariableOrToPart(messageActivity) || !hasOutputVariableOrFromPart(messageActivity))) {
            addViolation(messageActivity, INPUT_VARIABLE_OR_TO_PARTS_AND_OUTPUT_VARIABLE_OR_FROM_PART_FOR_INVOKE_MISSING);
        }
    }

    private void validateNoMessagePartConstraint(MessageActivity messageActivity) {
        if (hasToPartOrFromPart(messageActivity))
            addViolation(messageActivity, TO_PARTS_OR_FROM_PARTS_NOT_ALLOWED);
    }

    private boolean hasMessagePart(MessageActivity messageActivity)
            throws NavigationException {
        OperationElement operation = messageActivity.getOperation();

        if (isInvoke(messageActivity) || messageActivity.isReceiving()) {
            return operation.getInput().getMessage().hasAnyPart();
        } else if (isReply(messageActivity)) {
            return operation.getOutput().getMessage().hasAnyPart();
        } else {
            throw new NavigationException("WARNING: not a message activity..." + getBpelFileName());
        }

    }

    private boolean isInvoke(MessageActivity messageActivity) {
        return MessageActivity.Type.INVOKE.equals(messageActivity.getType());
    }

    private boolean isReply(MessageActivity messageActivity) {
        return MessageActivity.Type.REPLY.equals(messageActivity.getType());
    }

    private boolean hasToPartOrFromPart(MessageActivity messageActivity) {
        return (hasFromPart(messageActivity) || hasToPart(messageActivity));
    }

    private boolean hasToPart(MessageActivity messageActivity) {
        Nodes toPart = messageActivity.toXOM().query("child::bpel:toParts", CONTEXT);
        return toPart.hasAny();
    }

    private boolean hasFromPart(MessageActivity msgActivity) {
        Nodes fromPart = msgActivity.toXOM().query("child::bpel:fromParts", CONTEXT);
        return fromPart.hasAny();
    }

    private boolean hasVariable(MessageActivity msgActivity) {
        Nodes variable = msgActivity.toXOM().query("attribute::variable", CONTEXT);
        return variable.hasAny();
    }

    private boolean hasVariableOrFromPart(MessageActivity messageActivity) {
        return (hasVariable(messageActivity) || hasFromPart(messageActivity));
    }

    private boolean hasVariableOrToPart(MessageActivity messageActivity) {
        return (hasVariable(messageActivity) || hasToPart(messageActivity));
    }

    private boolean hasInputVariableOrToPart(MessageActivity messageActivity) {
        return (navigator.hasInputVariable(messageActivity) || hasToPart(messageActivity));
    }

    private boolean hasOutputVariableOrFromPart(MessageActivity messageActivity) {
        return (navigator.hasOutputVariable(messageActivity) || hasFromPart(messageActivity));
    }

    @Override
    public int getSaNumber() {
        return 47;
    }
}
