package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.fct.CompensateScopeElement;
import isabel.model.bpel.fct.CompensateTargetable;
import isabel.tool.impl.ValidationCollector;

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
