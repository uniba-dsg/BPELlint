package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.fct.CatchElement;

public class SA00081Validator extends Validator {

	public SA00081Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
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
