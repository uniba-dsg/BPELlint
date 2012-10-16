package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00052Validator extends Validator {
	public SA00052Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes invokeNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokeNodes) {
			Nodes fromParts = invoke.query("bpel:fromParts", CONTEXT);
			String outputVariable = new NodeHelper(invoke).getAttributeByName("outputVariable");

			if (!outputVariable.isEmpty() && fromParts.size() > 0) {
				addViolation(fileName, invoke, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 52;
	}
}
