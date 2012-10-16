package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00006Validator extends Validator {

	public SA00006Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		String filePath = fileHandler.getBpel().getFilePath();
		Nodes rethrows = fileHandler.getBpel().getDocument()
				.query("//bpel:rethrow", CONTEXT);

		for (Node actualRethrow : rethrows) {
			if (isNotInFaultHandler(actualRethrow)) {
				addViolation(filePath, actualRethrow, 1);
			}
		}
	}

	private boolean isNotInFaultHandler(Node actualRethrow) {
		return actualRethrow.query("ancestor::*[name()=\"faultHandlers\"]",
				CONTEXT).size() == 0;
	}

	@Override
	public int getSaNumber() {
		return 6;
	}

}
