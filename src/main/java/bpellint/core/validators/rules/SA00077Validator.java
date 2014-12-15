package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.CompensateScopeElement;
import bpellint.core.model.bpel.fct.CompensateTargetable;

public class SA00077Validator extends Validator {

	public SA00077Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (CompensateScopeElement compensateScope : processContainer.getAllCompensateScopes()) {
			if (!hasTarget(compensateScope)) {
				addViolation(compensateScope);
			}
		}

	}

	private boolean hasTarget(CompensateScopeElement compensateScope) {
		try {
			CompensateTargetable target = compensateScope.getTarget();
			return compensateScope.getEnclosingScope().equals(target.getEnclosingFctBarrier());
		} catch (NavigationException e) {
			return false;
		}
	}

	@Override
	public int getSaNumber() {
		return 77;
	}

}
