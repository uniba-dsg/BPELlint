package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.RethrowElement;

public class SA00006Validator extends Validator {

	public SA00006Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
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
