package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00063Validator extends Validator {

	public SA00063Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes onMessages = fileHandler.getBpel().getDocument()
				.query("//bpel:onMessage", CONTEXT);

		for (Node node : onMessages) {

			if (node.query("bpel:fromParts", CONTEXT).size() > 0
					&& node.query("@variable", CONTEXT).size() > 0) {
				addViolation(fileName, node, 1);
			}
		}
		return valid;
	}

	@Override
	public int getSaNumber() {
		return 63;
	}
}
