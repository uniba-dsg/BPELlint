package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00063Validator extends Validator {

	public SA00063Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes onMessages = fileHandler.getBpel().getDocument()
				.query("//bpel:onMessage", CONTEXT);

		for (Node node : onMessages) {

			if (node.query("bpel:fromParts", CONTEXT).size() > 0
					&& node.query("@variable", CONTEXT).size() > 0) {
				addViolation(fileName, node, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 63;
	}
}
