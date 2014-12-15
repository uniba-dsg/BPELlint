package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.InvokeElement;
import bpellint.core.model.bpel.mex.ReplyElement;
import bpellint.core.model.bpel.var.ToPartElement;
import bpellint.core.model.bpel.var.ToPartsElement;
import bpellint.core.model.wsdl.MessageElement;
import bpellint.core.model.wsdl.PartElement;
import nu.xom.Node;
import nu.xom.Nodes;

import static bpellint.core.model.Standards.CONTEXT;

public class SA00054Validator extends Validator {

	public SA00054Validator(ProcessContainer files, SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		hasPartForEveryToPart("//bpel:invoke");
		hasPartForEveryToPart("//bpel:reply");
	}

	private void hasPartForEveryToPart(String xPathOutgoingOperation) {
		Nodes outgoingOperations = processContainer.getBpel().getDocument()
				.query(xPathOutgoingOperation, CONTEXT);

		for (Node outgoingOperation : outgoingOperations) {
			try {
				MessageElement message;
				if ("invoke".equals(PrefixHelper.removePrefix(xPathOutgoingOperation))) {
					message = new InvokeElement(outgoingOperation, processContainer).getOperation()
							.getInput().getMessage();
				} else {
					message = new ReplyElement(outgoingOperation, processContainer).getOperation()
							.getOutput().getMessage();
				}

				Nodes toPartsNode = outgoingOperation.query("bpel:toParts", CONTEXT);
				if (!toPartsNode.hasAny()) {
					return;
				}
				ToPartsElement toParts = new ToPartsElement(toPartsNode.get(0), processContainer);
				for (ToPartElement toPart : toParts.getAllToParts()) {
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

	private boolean hasToPartCorrespondingMessagePart(ToPartElement toPart, MessageElement message) {
		try {
			for (PartElement part : message.getParts()) {
				if (part.getNameAttribute().equals(toPart.getPartAttribute())) {
					return true;
				}
			}
		} catch (NavigationException e) {
			// if the message has no part, then it cannot correspond to the
			// toPart@part
		}

		return false;
	}

	@Override
	public int getSaNumber() {
		return 54;
	}
}
