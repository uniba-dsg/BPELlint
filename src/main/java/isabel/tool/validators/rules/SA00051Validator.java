package isabel.tool.validators.rules;

import isabel.model.bpel.mex.InvokeElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

public class SA00051Validator extends Validator {

	public SA00051Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		for (InvokeElement invoke : fileHandler.getAllInvokes()) {
            if (invoke.hasToParts() && invoke.hasInputVariable()) {
				addViolation(invoke);
			}
		}
	}

    @Override
	public int getSaNumber() {
		return 51;
	}

}
