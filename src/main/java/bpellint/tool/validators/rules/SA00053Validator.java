package bpellint.tool.validators.rules;

import static bpellint.model.Standards.CONTEXT;
import static bpellint.model.bpel.mex.MessageActivity.Type.*;

import java.util.List;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.bpel.var.FromPartElement;
import bpellint.model.bpel.var.FromPartsElement;
import bpellint.model.wsdl.MessageElement;
import bpellint.model.wsdl.PartElement;
import bpellint.tool.validators.result.ValidationCollector;

import nu.xom.Nodes;

public class SA00053Validator extends Validator {

    public SA00053Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        hasPartForEveryFromPart(processContainer.getAllIncomingMessageActivities());
    }

    private void hasPartForEveryFromPart(List<MessageActivity> incomingOperations) {
        for (MessageActivity messageActivity : incomingOperations) {
            try {
                MessageElement message;
                if (INVOKE.equals(messageActivity.getType())) {
                    message = messageActivity.getOperation().getOutput().getMessage();
                } else {
                    message = messageActivity.getOperation().getInput().getMessage();
                } 

                Nodes fromPartsNode = messageActivity.toXOM().query("bpel:fromParts", CONTEXT);
                if (!fromPartsNode.hasAny()) {
                	continue;
                }
                FromPartsElement fromParts = new FromPartsElement(fromPartsNode.get(0), processContainer);

				for (FromPartElement fromPart : fromParts.getAllFromParts()) {
					if (!hasFromPartCorrespondingMessagePart(fromPart, message)) {
						addViolation(fromPart);
					}
				}
            } catch (NavigationException e) {
                // message does not exist, outgoingOperation is not validatable
                // => valid
            }

        }
    }

    private boolean hasFromPartCorrespondingMessagePart(FromPartElement fromPart, MessageElement message) {
		try {
			for (PartElement part : message.getParts()) {
				if (part.getNameAttribute().equals(fromPart.getPartAttribute())) {
					return true;
				}
			}
		} catch (NavigationException e) {
			// if the message has no part, then it cannot correspond to the fromPart@part
		}
        return false;
    }

    @Override
    public int getSaNumber() {
        return 53;
    }
}
