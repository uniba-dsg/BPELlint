package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;

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
				Node message;
				if ("invoke".equals(PrefixHelper
						.removePrefix(xPathOutgoingOperation))) {
					message = navigator
							.getCorrespondingOutgoingMessage(incomingOperation);
				} else {
					message = navigator
							.getCorrespondingIncomingMessage(incomingOperation);
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
