package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.ProcessElement;
import isabel.model.bpel.ScopeElement;
import isabel.tool.impl.ValidationCollector;

public class SA00091Validator extends Validator {

	public SA00091Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (ScopeElement scope : processContainer.getAllScopes()) {
			if (scope.isIsolated()) {
				checkContainsNoIsolatedScopes(scope);
			}
		}

	}

	private void checkContainsNoIsolatedScopes(ScopeElement isolatedScope) {
		for (ScopeElement scope : processContainer.getAllScopes()) {
			if (!isChild(scope, isolatedScope) || scope.equals(isolatedScope)) {
				continue;
			}
			if (scope.isIsolated()) {
				addViolation(isolatedScope);
			}
		}
	}

	private boolean isChild(ScopeElement scope, ScopeElement isolatedScope) {
		if (scope.equals(isolatedScope)) {
			return true;
		}
		if (scope instanceof ProcessElement) {
			return false;
		}
		return isChild(scope.getEnclosingScope(), isolatedScope);
	}

	@Override
	public int getSaNumber() {
		return 91;
	}

}
