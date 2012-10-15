package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00024Validator extends Validator {

	public SA00024Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes variables = fileHandler.getBpel().getDocument()
				.query("//bpel:variable/@name", CONTEXT);
		Nodes onEvents = fileHandler.getBpel().getDocument()
				.query("//bpel:onEvent/@variable", CONTEXT);

		for (Node node : variables) {
			if (node.getValue().contains(".")) {
				addViolation(fileName, node, 1);
			}
		}

		for (Node node : onEvents) {
			if (node.getValue().contains(".")) {
				addViolation(fileName, node, 1);
			}
		}

		return valid;
	}

	@Override
	public int getSaNumber() {
		return 24;
	}
}
