package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.CompensateScopeElement;
import bpellint.model.bpel.fct.CompensateTargetable;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00078Validator extends Validator {

	public SA00078Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (CompensateScopeElement compensateScope : processContainer
				.getAllCompensateScopes()) {
			try {
				CompensateTargetable targetScope = compensateScope.getTarget();
				if (!targetScope.hasCompensationHandler() && !targetScope.hasFaultHandler()) {
					addViolation(targetScope);
				}
			} catch (NavigationException e) {
				// ignore target is missing, this is checked by SA00076
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 78;
	}

}
