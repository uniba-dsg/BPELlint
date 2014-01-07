package isabel.tool.validators.rules;

import static isabel.model.bpel.mex.MessageActivity.Type.*;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.bpel.mex.MessageActivityImpl;
import isabel.model.bpel.var.FromPartElement;
import isabel.model.bpel.var.FromPartsElement;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
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
        hasPartForEveryFromPart(fileHandler.getAllIncommingMessageActivities());
    }

    private void hasPartForEveryFromPart(List<MessageActivity> incomingOperations) {
        for (MessageActivity messageActivity : incomingOperations) {
            try {
                MessageElement message;
                if (INVOKE.equals(messageActivity.getType())) {
                    message = messageActivity.getOperation().getOutput().getMessage();
                } else if(messageActivity.isReceiving()) {
                    message = messageActivity.getOperation().getInput().getMessage();
                } else {
                    throw new IllegalStateException("Should not happen!");
                }

                Nodes fromPartsNode = messageActivity.toXOM().query("bpel:fromParts", CONTEXT);
                if (!fromPartsNode.hasAny()) {
                	continue;
                }
                FromPartsElement fromParts = new FromPartsElement(fromPartsNode.get(0), fileHandler);

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
