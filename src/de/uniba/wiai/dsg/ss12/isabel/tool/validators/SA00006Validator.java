package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00006Validator extends Validator {

	public SA00006Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		String filePath = fileHandler.getBpel().getFilePath();
		Nodes rethrows = fileHandler.getBpel().getDocument()
				.query("//bpel:rethrow", CONTEXT);

		for (Node actualRethrow : rethrows) {
			if (isNotInFaultHandler(actualRethrow)) {
				addViolation(filePath, actualRethrow, 1);
			}
		}
		return valid;
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
