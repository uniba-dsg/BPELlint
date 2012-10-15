package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00051Validator extends Validator {

	public SA00051Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes invokes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node node : invokes) {

			Nodes toPartsSet = node.query("bpel:toParts", CONTEXT);
			Nodes inputVariableSet = node.query("@inputVariable", CONTEXT);
			if ((toPartsSet.size() > 0) && (inputVariableSet.size() > 0)) {
				addViolation(fileName, node, 1);
			}
		}
		return valid;
	}

	@Override
	public int getSaNumber() {
		return 51;
	}

}
