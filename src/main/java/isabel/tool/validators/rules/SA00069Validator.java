package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.flow.TargetElement;
import isabel.model.bpel.flow.TargetsElement;
import isabel.tool.validators.result.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

public class SA00069Validator extends Validator {

	public SA00069Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
