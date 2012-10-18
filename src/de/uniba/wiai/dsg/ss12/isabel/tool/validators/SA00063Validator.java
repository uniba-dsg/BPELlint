package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00063Validator extends Validator {

	public SA00063Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes onMessages = fileHandler.getBpel().getDocument()
				.query("//bpel:onMessage", CONTEXT);

		for (Node node : onMessages) {

			if (node.query("bpel:fromParts", CONTEXT).size() > 0
					&& node.query("@variable", CONTEXT).size() > 0) {
				addViolation(node);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 63;
	}
}
