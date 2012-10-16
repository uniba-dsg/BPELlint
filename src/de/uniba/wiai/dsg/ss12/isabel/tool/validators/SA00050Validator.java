package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.PrefixHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00050Validator extends Validator {

	private String filePath;

	public SA00050Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		filePath = fileHandler.getBpel().getFilePath();

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
				if ("invoke".equals(PrefixHelper.removePrefix(xPathOutgoingOperation))) {
					message = navigator
							.getCorrespondingIncomingMessage(outgoingOperation);
				} else {
					message = navigator
							.getCorrespondingOutgoingMessage(outgoingOperation);
				}
				Nodes messageParts = message.query("wsdl:part", CONTEXT);

				if (toParts.size() > 0) {
					for (Node part : messageParts) {

						if (!hasMessagePartCorrespondingToPart(part, toParts)) {
							addViolation(filePath, outgoingOperation, 1);
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
		String partName = new NodeHelper(part).getAttributeByName("name");

		for (Node toPart : toParts) {
			String partAttribute = new NodeHelper(toPart).getAttributeByName("part");

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
