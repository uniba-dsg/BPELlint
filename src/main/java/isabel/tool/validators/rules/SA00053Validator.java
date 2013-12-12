package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.bpel.mex.MessageActivityImpl;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00053Validator extends Validator {

    public SA00053Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        hasPartForEveryFromPart("//bpel:invoke");
        hasPartForEveryFromPart("//bpel:receive");
        hasPartForEveryFromPart("//bpel:onMessage");
        hasPartForEveryFromPart("//bpel:onEvent");
    }

    private void hasPartForEveryFromPart(String xPathOutgoingOperation) {
        Nodes incomingOperations = fileHandler.getBpel().getDocument()
                .query(xPathOutgoingOperation, CONTEXT);

        for (Node incomingOperation : incomingOperations) {
            try {

                MessageActivityImpl messageActivity = new MessageActivityImpl(new NodeHelper(incomingOperation), fileHandler);
                Node message;
                if (MessageActivity.Type.INVOKE.equals(messageActivity.getType())) {
                    message = navigator.getCorrespondingOutgoingMessage(messageActivity);
                } else if(messageActivity.isReceiving()) {
                    message = navigator.getCorrespondingIncomingMessage(messageActivity);
                } else {
                    throw new IllegalStateException("Should not happen!");
                }

                Nodes fromParts = incomingOperation.query(
                        "bpel:fromParts/bpel:fromPart", CONTEXT);

                if (fromParts.hasAny()) {
                    for (Node fromPart : fromParts) {

                        if (!hasFromPartCorrespondingMessagePart(fromPart, message)) {
                            addViolation(fromPart);
                        }
                    }
                }
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private boolean hasFromPartCorrespondingMessagePart(Node fromPart,
                                                        Node message) {
        Nodes messageParts = message.query("wsdl:part", CONTEXT);

        if (messageParts.hasAny()) {
            String partAttribute = new NodeHelper(fromPart).getAttribute("part");

            for (Node part : messageParts) {
                String partName = new NodeHelper(part).getAttribute("name");

                if (partName.equals(partAttribute)) {
                    return true;
                }

            }
        }
        return false;
    }

    @Override
    public int getSaNumber() {
        return 53;
    }
}
