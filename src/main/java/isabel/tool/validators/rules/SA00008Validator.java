package isabel.tool.validators.rules;

import isabel.model.bpel.CompensateElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

public class SA00008Validator extends Validator {

	public SA00008Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (CompensateElement compensate : fileHandler.getAllCompensates()) {

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
