package bpellint.tool.validators.rules;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.fct.CatchElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00081Validator extends Validator {

	public SA00081Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (CatchElement catchElement : processContainer.getAllCatch()) {
			if (!hasDefinitType(catchElement)) {
				addViolation(catchElement);
			}
		}
	}

	private boolean hasDefinitType(CatchElement catchElement) {
		return hasFaultMessageTypeVariable(catchElement)
				|| hasFaultElementVariable(catchElement)
				|| hasNoVariable(catchElement);
	}

	private boolean hasFaultMessageTypeVariable(CatchElement catchElement) {
		return catchElement.hasFaultVariable()
				&& catchElement.hasVariableMessageType()
				&& !catchElement.hasVariableElement();
	}

	private boolean hasFaultElementVariable(CatchElement catchElement) {
		return catchElement.hasFaultVariable()
				&& !catchElement.hasVariableMessageType()
				&& catchElement.hasVariableElement();
	}

	private boolean hasNoVariable(CatchElement catchElement) {
		return !catchElement.hasFaultVariable()
				&& !catchElement.hasVariableMessageType()
				&& !catchElement.hasVariableElement();
	}

	@Override
	public int getSaNumber() {
		return 81;
	}

}
