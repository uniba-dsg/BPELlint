package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.CompensateScopeElement;
import bpellint.core.model.bpel.fct.CompensateTargetable;

public class SA00078Validator extends Validator {

	public SA00078Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
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
