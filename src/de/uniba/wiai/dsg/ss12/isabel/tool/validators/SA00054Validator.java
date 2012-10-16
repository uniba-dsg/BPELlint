package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.PrefixHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00054Validator extends Validator {

	private String filePath;

	public SA00054Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		filePath = fileHandler.getBpel().getFilePath();

		hasPartForEveryToPart("//bpel:invoke");
		hasPartForEveryToPart("//bpel:reply");
	}

	private void hasPartForEveryToPart(String xPathOutgoingOperation) {
		Nodes outgoingOperations = fileHandler.getBpel().getDocument()
				.query(xPathOutgoingOperation, CONTEXT);

		for (Node outgoingOperation : outgoingOperations) {
			try {
				Node message;
				if ("invoke".equals(PrefixHelper.removePrefix(xPathOutgoingOperation))) {
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
						addViolation(filePath, toPart, 1);
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
			String partAttribute = new NodeHelper(toPart).getAttributeByName("part");

			for (Node part : messageParts) {
				String partName = new NodeHelper(part).getAttributeByName("name");

				if (partName.equals(partAttribute)) {
					return true;
				}

			}

			return false;
		} else {
			return thereIsAToPartButNoPart();
		}
	}

	private boolean thereIsAToPartButNoPart() {
		return false;
	}

	@Override
	public int getSaNumber() {
		return 54;
	}
}
