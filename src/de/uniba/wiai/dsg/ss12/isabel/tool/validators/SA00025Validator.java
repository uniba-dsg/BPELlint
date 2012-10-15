package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00025Validator extends Validator {

	public SA00025Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes variables = fileHandler.getBpel().getDocument()
				.query("//bpel:variable", CONTEXT);

		for (Node node : variables) {

			Nodes messageTypeSet = node.query("@messageType", CONTEXT);
			Nodes typeSet = node.query("@type", CONTEXT);
			Nodes elementSet = node.query("@element", CONTEXT);

			if ((messageTypeSet.size() == 0) && (typeSet.size() == 0)
					&& (elementSet.size() == 0)) {
				addViolation(fileName, node, 1);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0) {
				addViolation(fileName, node, 2);
			} else if (messageTypeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(fileName, node, 3);
			} else if (typeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(fileName, node, 4);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0
					&& elementSet.size() > 0) {
				addViolation(fileName, node, 5);
			}
		}
		return valid;
	}

	@Override
	public int getSaNumber() {
		return 25;
	}
}
