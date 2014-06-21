package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.CompensateScopeElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00007Validator extends Validator {

	public SA00007Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
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
