package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.OnEventElement;
import isabel.tool.impl.ValidationCollector;

public class SA00085Validator extends Validator {

	public SA00085Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : fileHandler.getAllOnEvents()) {
			if (!onEvent.hasFromParts()) {
				continue;
			}
			if (onEvent.hasVariableAttribute() || onEvent.hasVariableElement() || onEvent.hasVariableMessageType()) {
				addViolation(onEvent);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 85;
	}

}
