package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.flow.TargetElement;
import bpellint.core.model.bpel.flow.TargetsElement;

public class SA00069Validator extends Validator {

	public SA00069Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (TargetsElement targets : processContainer.getAllTargetsContainer()) {
			checkLinkNameUniqueness(targets);
		}
	}

	private void checkLinkNameUniqueness(TargetsElement targets) {
		Set<String> uniqueNames = new HashSet<>();
		for (TargetElement target : targets.getAllTargets()) {
			String name = target.getLinkName();

			if (uniqueNames.contains(name)) {
				addViolation(target);
			} else {
				uniqueNames.add(name);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 69;
	}

}
