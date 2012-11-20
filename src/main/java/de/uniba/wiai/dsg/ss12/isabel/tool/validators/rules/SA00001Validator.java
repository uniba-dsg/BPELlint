package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodesUtil;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.wsdl.OperationElement;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;

import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00001Validator extends Validator {

	private static final int SOLICIT_RESPONSE_TYPE = 2;
	private static final int NOTIFICATION_FAULT = 1;

	public SA00001Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			for (Node currentOperation : getOperations(wsdlEntry)) {
				OperationElement operationHelper = new OperationElement(currentOperation);
				if (operationHelper.isNotification()) {
					addViolation(currentOperation, NOTIFICATION_FAULT);
				}
				if (operationHelper.isSolicitResponse()) {
					addViolation(currentOperation, SOLICIT_RESPONSE_TYPE);
				}
			}
		}
	}

	private List<Node> getOperations(DocumentEntry wsdlEntry) {
		return NodesUtil.toList(wsdlEntry.getDocument().query(
				"//wsdl:portType/wsdl:operation", CONTEXT));
	}

	@Override
	public int getSaNumber() {
		return 1;
	}

}
