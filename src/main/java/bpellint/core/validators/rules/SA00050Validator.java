package bpellint.core.validators.rules;

import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.InvokeElement;
import bpellint.core.model.bpel.mex.ReplyElement;
import bpellint.core.model.bpel.var.ToPartElement;
import bpellint.core.model.bpel.var.ToPartsElement;
import bpellint.core.model.wsdl.MessageElement;
import bpellint.core.model.wsdl.PartElement;

import nu.xom.Nodes;

import static bpellint.core.model.Standards.CONTEXT;

public class SA00050Validator extends Validator {

    public SA00050Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
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
