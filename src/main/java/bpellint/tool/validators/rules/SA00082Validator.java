package bpellint.tool.validators.rules;


import java.util.Set;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.ScopeElement;
import bpellint.tool.validators.result.ValidationCollector;

import com.google.common.collect.Sets;

public class SA00082Validator extends Validator {

	public SA00082Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
