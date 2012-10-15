package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00059Validator extends Validator {

	public SA00059Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes replys = fileHandler.getBpel().getDocument()
				.query("//bpel:reply", CONTEXT);

		for (Node node : replys) {

			Nodes toPartsSet = node.query("bpel:toParts", CONTEXT);
			Nodes variableSet = node.query("@variable", CONTEXT);
			if ((toPartsSet.size() > 0) && (variableSet.size() > 0)) {
				addViolation(fileName, node, 1);
			}
		}
		return valid;
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}