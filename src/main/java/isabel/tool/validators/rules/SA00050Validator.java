package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00050Validator extends Validator {

    public SA00050Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        hasToPartForEveryMessagePartInInvokes();
        hasToPartForEveryMessagePartInReplies();
    }

    private void hasToPartForEveryMessagePartInInvokes() {
        for (InvokeElement invoke : fileHandler.getAllInvokes()) {
            try {
                Nodes toParts = invoke.toXOM().query("bpel:toParts/bpel:toPart", CONTEXT);

                Node message = navigator.getCorrespondingIncomingMessage(invoke);
                Nodes messageParts = message.query("wsdl:part", CONTEXT);

                if (toParts.hasAny()) {
                    for (Node part : messageParts) {

                        if (!hasMessagePartCorrespondingToPart(part, toParts)) {
                            addViolation(invoke);
                        }
                    }
                }
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private void hasToPartForEveryMessagePartInReplies() {
        for (ReplyElement reply : fileHandler.getAllReplies()) {
            try {
                Nodes toParts = reply.toXOM().query("bpel:toParts/bpel:toPart", CONTEXT);

                Node message = navigator.getCorrespondingOutgoingMessage(reply);
                Nodes messageParts = message.query("wsdl:part", CONTEXT);

                if (toParts.hasAny()) {
                    for (Node part : messageParts) {

                        if (!hasMessagePartCorrespondingToPart(part, toParts)) {
                            addViolation(reply);
                        }
                    }
                }
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private boolean hasMessagePartCorrespondingToPart(Node part, Nodes toParts) {
        String partName = new NodeHelper(part).getAttribute("name");

        for (Node toPart : toParts) {
            String partAttribute = new NodeHelper(toPart).getAttribute("part");

            if (partName.equals(partAttribute)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public int getSaNumber() {
        return 50;
    }

}
