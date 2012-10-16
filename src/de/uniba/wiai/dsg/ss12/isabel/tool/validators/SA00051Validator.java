package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00051Validator extends Validator {

	public SA00051Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes invokes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokes) {

			Nodes toPartsSet = invoke.query("bpel:toParts", CONTEXT);
			Nodes inputVariableSet = invoke.query("@inputVariable", CONTEXT);
			if ((toPartsSet.size() > 0) && (inputVariableSet.size() > 0)) {
				addViolation(fileName, invoke, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 51;
	}

}
