package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.InvokeElement;

public class SA00051Validator extends Validator {

	public SA00051Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		for (InvokeElement invoke : processContainer.getAllInvokes()) {
            if (invoke.hasToParts() && invoke.hasInputVariable()) {
				addViolation(invoke);
			}
		}
	}

    @Override
	public int getSaNumber() {
		return 51;
	}

}
