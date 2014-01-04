package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.model.bpel.var.ToPartElement;
import isabel.model.bpel.var.ToPartsElement;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00054Validator extends Validator {

	public SA00054Validator(ProcessContainer files, ValidationCollector violationCollector) {
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
				MessageElement message;
				if ("invoke".equals(PrefixHelper.removePrefix(xPathOutgoingOperation))) {
					message = new InvokeElement(outgoingOperation, fileHandler).getOperation()
							.getInput().getMessage();
				} else {
					message = new ReplyElement(outgoingOperation, fileHandler).getOperation()
							.getOutput().getMessage();
				}

				Nodes toPartsNode = outgoingOperation.query("bpel:toParts", CONTEXT);
				if (!toPartsNode.hasAny()) {
					return;
				}
				ToPartsElement toParts = new ToPartsElement(toPartsNode.get(0), fileHandler);
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
