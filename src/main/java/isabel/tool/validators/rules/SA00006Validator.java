package isabel.tool.validators.rules;

import isabel.model.bpel.RethrowElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

public class SA00006Validator extends Validator {

	public SA00006Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (RethrowElement rethrow : fileHandler.getAllRethrows()) {

			if (!rethrow.isWithinFaultHandler()) {
				addViolation(rethrow);
			}

		}
	}

    @Override
	public int getSaNumber() {
		return 6;
	}

}
