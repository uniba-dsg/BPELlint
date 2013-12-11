package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
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
		hasToPartForEveryMessagePart("//bpel:invoke");
		hasToPartForEveryMessagePart("//bpel:reply");
	}

	private void hasToPartForEveryMessagePart(String xPathOutgoingOperation) {
		Nodes outgoingOperations = fileHandler.getBpel().getDocument()
				.query(xPathOutgoingOperation, CONTEXT);

		for (Node outgoingOperation : outgoingOperations) {
			try {
				Nodes toParts = outgoingOperation.query(
						"bpel:toParts/bpel:toPart", CONTEXT);

				Node message;
				if ("invoke".equals(PrefixHelper
						.removePrefix(xPathOutgoingOperation))) {
					message = navigator
							.getCorrespondingIncomingMessage(outgoingOperation);
				} else {
					message = navigator
							.getCorrespondingOutgoingMessage(outgoingOperation);
				}
				Nodes messageParts = message.query("wsdl:part", CONTEXT);

				if (toParts.hasAny()) {
					for (Node part : messageParts) {

						if (!hasMessagePartCorrespondingToPart(part, toParts)) {
							addViolation(outgoingOperation);
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
