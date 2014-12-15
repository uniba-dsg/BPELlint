package bpellint.core.validators.rules;


import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;

import com.google.common.collect.Sets;

public class SA00082Validator extends Validator {

	public SA00082Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (ScopeElement scope : processContainer.getAllScopes()) {
			Set<String> ownSources = scope.getSourceLinkNames();
			Set<String> ownTargets = scope.getTargetLinkNames();
			for (ScopeElement peerScope : scope.getPeerScopes()) {
				if (Sets.intersection(ownSources, peerScope.getTargetLinkNames()).isEmpty()) {
					continue;
				}
				if (!Sets.intersection(ownTargets, peerScope.getSourceLinkNames()).isEmpty()) {
					addViolation(scope);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 82;
	}

}
