package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.ReceiveHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00055Validator extends Validator {

	public SA00055Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes receives = fileHandler.getBpel().getDocument()
				.query("//bpel:receive", CONTEXT);

		for (Node receive : receives) {
			ReceiveHelper receiveHelper = new ReceiveHelper(receive);

			if (receiveHelper.hasFromParts() && receiveHelper.hasVariable()) {
				addViolation(receive);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}