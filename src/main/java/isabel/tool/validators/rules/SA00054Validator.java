package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00054Validator extends Validator {

    public SA00054Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        hasPartForEveryToPart("//bpel:invoke");
        hasPartForEveryToPart("//bpel:reply");
    }

    private void hasPartForEveryToPart(String xPathOutgoingOperation) {
        Nodes outgoingOperations = fileHandler.getBpel().getDocument()
                .query(xPathOutgoingOperation, CONTEXT);

        for (Node outgoingOperation : outgoingOperations) {
            try {
                Node message;
                if ("invoke".equals(PrefixHelper
                        .removePrefix(xPathOutgoingOperation))) {
                    message = navigator.getCorrespondingIncomingMessage(new InvokeElement(outgoingOperation, fileHandler));
                } else {
                    message = navigator.getCorrespondingOutgoingMessage(new ReplyElement(outgoingOperation, fileHandler));
                }

                Nodes toParts = outgoingOperation.query(
                        "bpel:toParts/bpel:toPart", CONTEXT);
                for (Node toPart : toParts) {

                    if (!hasToPartCorrespondingMessagePart(toPart, message)) {
                        addViolation(toPart);
                    }
                }
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private boolean hasToPartCorrespondingMessagePart(Node toPart, Node message) {
        Nodes messageParts = message.query("wsdl:part", CONTEXT);

        if (messageParts.hasAny()) {
            String partAttribute = new NodeHelper(toPart).getAttribute("part");

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
        return 54;
    }
}
