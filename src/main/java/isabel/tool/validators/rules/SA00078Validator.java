package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.fct.CompensateScopeElement;
import isabel.model.bpel.fct.CompensateTargetable;
import isabel.tool.validators.result.ValidationCollector;

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
