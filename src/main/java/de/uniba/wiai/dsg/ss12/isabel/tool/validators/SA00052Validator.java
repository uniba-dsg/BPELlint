package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00052Validator extends Validator {
	public SA00052Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes invokeNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokeNodes) {
			Nodes fromParts = invoke.query("bpel:fromParts", CONTEXT);
			String outputVariable = new NodeHelper(invoke).getAttribute("outputVariable");

			if (!outputVariable.isEmpty() && fromParts.size() > 0) {
				addViolation(invoke);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 52;
	}
}
