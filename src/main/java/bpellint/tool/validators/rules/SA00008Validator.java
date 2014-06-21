package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.CompensateElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00008Validator extends Validator {

	public SA00008Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
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
