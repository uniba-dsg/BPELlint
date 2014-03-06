package isabel.tool.validators.rules;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.model.bpel.var.ToPartElement;
import isabel.model.bpel.var.ToPartsElement;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
import isabel.tool.validators.result.ValidationCollector;
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
        for (InvokeElement invoke : processContainer.getAllInvokes()) {
            try {
                Nodes toPartsNode = invoke.toXOM().query("bpel:toParts", CONTEXT);
                if (!toPartsNode.hasAny()) {
                	return;
                }
                List<ToPartElement> toParts = new ToPartsElement(toPartsNode.get(0), processContainer).getAllToParts();

                MessageElement message = invoke.getOperation().getInput().getMessage();
                List<PartElement> messageParts = message.getParts();

				for (PartElement part : messageParts) {
					if (!hasMessagePartCorrespondingToPart(part, toParts)) {
						addViolation(invoke);
					}
				}
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private void hasToPartForEveryMessagePartInReplies() {
        for (ReplyElement reply : processContainer.getAllReplies()) {
            try {
            	Nodes toPartsNode = reply.toXOM().query("bpel:toParts", CONTEXT);
                if (!toPartsNode.hasAny()) {
                	return;
                }
                List<ToPartElement> toParts = new ToPartsElement(toPartsNode.get(0), processContainer).getAllToParts();

                MessageElement message = reply.getOperation().getOutput().getMessage();
				for (PartElement part : message.getParts()) {
					if (!hasMessagePartCorrespondingToPart(part, toParts)) {
						addViolation(reply);
					}
				}
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private boolean hasMessagePartCorrespondingToPart(PartElement part, List<ToPartElement> toParts) {
        for (ToPartElement toPart : toParts) {
            if (part.getNameAttribute().equals(toPart.getPartAttribute())) {
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
