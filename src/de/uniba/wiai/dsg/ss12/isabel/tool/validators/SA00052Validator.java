package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00052Validator extends Validator {
	public SA00052Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes invokeNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokeNodes) {
			Nodes fromParts = invoke.query("bpel:fromParts", CONTEXT);
			String outputVariable = getAttributeValue(invoke.query(
					"@outputVariable", CONTEXT));

			if (!outputVariable.isEmpty() && fromParts.size() > 0) {
				addViolation(fileName, invoke, 1);
			}
		}
		return valid;
	}

	@Override
	public int getSaNumber() {
		return 52;
	}
}
