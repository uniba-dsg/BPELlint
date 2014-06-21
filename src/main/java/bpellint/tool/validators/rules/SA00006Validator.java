package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.RethrowElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00006Validator extends Validator {

	public SA00006Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (RethrowElement rethrow : processContainer.getAllRethrows()) {

			if (!rethrow.isWithinFaultHandler()) {
				addViolation(rethrow);
			}

		}
	}

    @Override
	public int getSaNumber() {
		return 6;
	}

}
