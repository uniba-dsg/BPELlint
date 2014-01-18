package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.ScopeElement;
import isabel.tool.impl.ValidationCollector;

import java.util.Set;

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
