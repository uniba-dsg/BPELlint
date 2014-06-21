package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.InvokeElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00051Validator extends Validator {

	public SA00051Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
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
