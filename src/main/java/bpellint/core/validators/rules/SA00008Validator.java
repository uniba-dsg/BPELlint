package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.CompensateElement;

public class SA00008Validator extends Validator {

	public SA00008Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (CompensateElement compensate : processContainer.getAllCompensates()) {

            if (!compensate.isWithinFaultHandler()
                    && !compensate.isWithinCompensationHandler()
                    && !compensate.isWithinTerminationHandler()) {
                addViolation(compensate);
            }
		}
	}

	@Override
	public int getSaNumber() {
		return 8;
	}

}
