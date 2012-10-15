package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00001Validator extends Validator {

	private String filePath;

	public SA00001Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			filePath = wsdlEntry.getFilePath();
			Nodes operations = getOperations(wsdlEntry);
			for (int i = 0; i < operations.size(); i++) {
				Node currentOperation = operations.get(i);

				isNotification(currentOperation);
				isSolicitResponse(currentOperation);
			}
		}
		return valid;
	}

	private void isNotification(Node currentOperation) {
		if (hasOutput(currentOperation) && !hasInput(currentOperation)) {
			addViolation(filePath, currentOperation, 1);
		}
	}

	private void isSolicitResponse(Node currentOperation) {

		if (hasOutput(currentOperation) && hasInput(currentOperation)) {
			if (isFirstChildOutput(currentOperation)) {
				addViolation(filePath, currentOperation, 2);
			}
		}
	}

	private boolean isFirstChildOutput(Node currentOperation) {
		Node firstOperationChild = currentOperation.query("(child::*)[1]").get(
				0);
		Node operationOutput = currentOperation.query("child::wsdl:output",
				CONTEXT).get(0);
		return firstOperationChild.equals(operationOutput);
	}

	private Nodes getOperations(DocumentEntry wsdlEntry) {
		Nodes operations = wsdlEntry.getDocument().query(
				"//wsdl:portType/wsdl:operation", CONTEXT);
		return operations;
	}

	private boolean hasOutput(Node currentOperation) {
		boolean outputExists = currentOperation.query("child::wsdl:output",
				CONTEXT).size() > 0;
		return outputExists;
	}

	private boolean hasInput(Node currentOperation) {
		boolean inputExists = currentOperation.query("child::wsdl:input",
				CONTEXT).size() > 0;
		return inputExists;
	}

	@Override
	public int getSaNumber() {
		return 1;
	}

}
