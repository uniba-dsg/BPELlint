package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00055Validator extends Validator {

	public SA00055Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes receives = fileHandler.getBpel().getDocument()
				.query("//bpel:receive", CONTEXT);

		for (Node receive : receives) {
			ReceiveHelper receiveHelper = new ReceiveHelper(receive);

			if (receiveHelper.hasFromParts() && receiveHelper.hasVariable()) {
				addViolation(fileName, receive, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}