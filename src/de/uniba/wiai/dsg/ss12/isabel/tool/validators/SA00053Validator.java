package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.prefixFree;

public class SA00053Validator extends Validator {

	private String filePath;

	public SA00053Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		filePath = fileHandler.getBpel().getFilePath();

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
				if ("invoke".equals(prefixFree(xPathOutgoingOperation))) {
					message = navigator
							.getCorrespondingOutgoingMessage(incomingOperation);
				} else {
					message = navigator
							.getCorrespondingIncomingMessage(incomingOperation);
				}

				Nodes fromParts = incomingOperation.query(
						"bpel:fromParts/bpel:fromPart", CONTEXT);

				if (fromParts.size() > 0) {
					for (Node fromPart : fromParts) {

						if (!hasFromPartCorrespondingMessagePart(fromPart,
								message)) {
							addViolation(filePath, fromPart, 1);
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

		if (messageParts.size() > 0) {
			String partAttribute = getAttributeValue(fromPart.query("@part",
					CONTEXT));

			for (Node part : messageParts) {
				String partName = getAttributeValue(part
						.query("@name", CONTEXT));

				if (partName.equals(partAttribute)) {
					return true;
				}

			}

			return false;
		} else {
			return thereIsAFromPartButNoPart();
		}
	}

	private boolean thereIsAFromPartButNoPart() {
		return false;
	}

	@Override
	public int getSaNumber() {
		return 53;
	}
}
