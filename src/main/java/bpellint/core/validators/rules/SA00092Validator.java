package bpellint.core.validators.rules;

import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;


public class SA00092Validator extends Validator {

	public SA00092Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (ScopeElement scope : processContainer.getAllScopes()) {
			checkForScopeNameDuplicates(scope);
		}
	}

	private void checkForScopeNameDuplicates(ScopeElement scope) {
		Set<String> scopeNames = new HashSet<>();
		String scopeName = scope.getNameAttribute();
		if (scopeName.isEmpty()) {
			return;
		}
		scopeNames.add(scopeName);
		for (ScopeElement peerScope : scope.getPeerScopes()) {
			String peerScopeName = peerScope.getNameAttribute();
			if (peerScopeName.isEmpty()) {
				continue;
			}
			if (!scopeNames.add(peerScopeName)) {
				addViolation(scope);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 92;
	}

}
