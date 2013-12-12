package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.Map;

import static isabel.model.Standards.CONTEXT;

public class SA00047Validator extends Validator {

    public SA00047Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {

        for (MessageActivity messageActivity : fileHandler.getMessageActivities()) {
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
            addViolation(messageActivity, 2);
        } else if (isReply(messageActivity)
                && !hasVariableOrToPart(messageActivity)) {
            addViolation(messageActivity, 3);
        } else if (isInvoke(messageActivity)) {
            validateInvokeMessagePartConstraint(messageActivity);
        }
    }

    private void validateInvokeMessagePartConstraint(MessageActivity messageActivity)
            throws NavigationException {
        OperationElement operation = messageActivity.getOperation();
        if (operation.isOneWay()
                && !hasInputVariableOrToPart(messageActivity)) {
            addViolation(messageActivity, 4);
        } else if (operation.isRequestResponse()
                && (!hasInputVariableOrToPart(messageActivity) || !hasOutputVariableOrFromPart(messageActivity))) {
            addViolation(messageActivity, 5);
        }
    }

    private void validateNoMessagePartConstraint(MessageActivity messageActivity) {
        if (hasToPartOrFromPart(messageActivity))
            addViolation(messageActivity, 1);
    }

    private boolean hasMessagePart(MessageActivity messageActivity)
            throws NavigationException {
        OperationElement operation = messageActivity.getOperation();
        if (operation == null)
            throw new NavigationException("WARNING: could not found an associated operation " + getBpelFileName());

        Map<String, Node> messages = navigator.getOperationMessages(fileHandler.getWsdls(), operation);
        if (messages == null || messages.isEmpty())
            throw new NavigationException("WARNING: could not found an associated message in " + getBpelFileName());

        if (isInvoke(messageActivity)
                || messageActivity.isReceiving()) {
            return messages.get("input").query("child::*").hasAny();
        } else if (isReply(messageActivity)) {
            return messages.get("output").query("child::*").hasAny();
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
