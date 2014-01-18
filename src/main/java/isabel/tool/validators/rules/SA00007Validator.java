package isabel.tool.validators.rules;

import isabel.model.bpel.fct.CompensateScopeElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

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
