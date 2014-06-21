package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.CompensateScopeElement;
import bpellint.model.bpel.fct.CompensateTargetable;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00077Validator extends Validator {

	public SA00077Validator(ProcessContainer files, ValidationCollector validationCollector) {
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
