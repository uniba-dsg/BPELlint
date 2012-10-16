package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00055Validator extends Validator {

	public SA00055Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes receives = fileHandler.getBpel().getDocument()
				.query("//bpel:receive", CONTEXT);

		for (Node node : receives) {

			if ((node.query("bpel:fromParts", CONTEXT).size() > 0)
					&& (node.query("@variable", CONTEXT).size() > 0)) {
				addViolation(fileName, node, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}