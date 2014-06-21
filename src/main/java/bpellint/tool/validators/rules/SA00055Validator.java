package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.ReceiveElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00055Validator extends Validator {

	public SA00055Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
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