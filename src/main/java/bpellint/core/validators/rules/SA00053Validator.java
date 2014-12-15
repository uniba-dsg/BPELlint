package bpellint.core.validators.rules;

import static bpellint.core.model.Standards.CONTEXT;
import static bpellint.core.model.bpel.mex.MessageActivity.Type.*;

import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.MessageActivity;
import bpellint.core.model.bpel.var.FromPartElement;
import bpellint.core.model.bpel.var.FromPartsElement;
import bpellint.core.model.wsdl.MessageElement;
import bpellint.core.model.wsdl.PartElement;

import nu.xom.Nodes;

public class SA00053Validator extends Validator {

    public SA00053Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
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
