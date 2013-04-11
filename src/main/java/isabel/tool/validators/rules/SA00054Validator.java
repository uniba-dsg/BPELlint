package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;

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
					message = navigator
							.getCorrespondingIncomingMessage(outgoingOperation);
				} else {
					message = navigator
							.getCorrespondingOutgoingMessage(outgoingOperation);
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

		if (messageParts.size() > 0) {
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
