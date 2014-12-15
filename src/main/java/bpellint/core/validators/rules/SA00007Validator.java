package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.CompensateScopeElement;

public class SA00007Validator extends Validator {

	public SA00007Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (CompensateScopeElement compensateScope : processContainer.getAllCompensateScopes()) {

			if (!compensateScope.isWithinFaultHandler()
					&& !compensateScope.isWithinCompensationHandler()
					&& !compensateScope.isWithinTerminationHandler()) {
				addViolation(compensateScope);
			}
		}
	}

    @Override
	public int getSaNumber() {
		return 7;
	}

}
