package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.ReceiveElement;

public class SA00055Validator extends Validator {

	public SA00055Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (ReceiveElement receive : processContainer.getAllReceives()) {
			if (receive.hasFromParts() && receive.hasVariable()) {
				addViolation(receive);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}