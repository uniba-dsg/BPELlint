package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00085Validator extends Validator {

	public SA00085Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
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
