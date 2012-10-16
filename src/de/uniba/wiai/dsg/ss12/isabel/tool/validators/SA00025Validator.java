package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00025Validator extends Validator {

	public SA00025Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes variables = fileHandler.getBpel().getDocument()
				.query("//bpel:variable", CONTEXT);

		for (Node variable : variables) {

			Nodes messageTypeSet = variable.query("@messageType", CONTEXT);
			Nodes typeSet = variable.query("@type", CONTEXT);
			Nodes elementSet = variable.query("@element", CONTEXT);

			if ((messageTypeSet.size() == 0) && (typeSet.size() == 0)
					&& (elementSet.size() == 0)) {
				addViolation(fileName, variable, 1);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0) {
				addViolation(fileName, variable, 2);
			} else if (messageTypeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(fileName, variable, 3);
			} else if (typeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(fileName, variable, 4);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0
					&& elementSet.size() > 0) {
				addViolation(fileName, variable, 5);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 25;
	}
}
