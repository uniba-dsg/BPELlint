package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00001Validator extends Validator {

	private String filePath;

	public SA00001Validator(BpelProcessFiles files,
	                        ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			filePath = wsdlEntry.getFilePath();
			for (Node currentOperation : getOperations(wsdlEntry)) {
				if (new OperationHelper(currentOperation).isNotification()) {
					addViolation(filePath, currentOperation, 1);
				}
				if (new OperationHelper(currentOperation).isSolicitResponse()) {
					addViolation(filePath, currentOperation, 2);
				}
			}
		}
	}

	private Nodes getOperations(DocumentEntry wsdlEntry) {
		return wsdlEntry.getDocument().query(
				"//wsdl:portType/wsdl:operation", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 1;
	}

}
