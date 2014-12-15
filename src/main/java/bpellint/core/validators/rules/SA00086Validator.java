package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.OnEventElement;
import bpellint.core.model.bpel.var.FromPartElement;
import bpellint.core.model.bpel.var.VariableElement;

public class SA00086Validator extends Validator {

	public SA00086Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			try {
				checkForExplicitVariableDuplication(onEvent);
			} catch (NavigationException e) {
				// ignore, no variable => no explicit variable definition
			}
		}
	}

	private void checkForExplicitVariableDuplication(OnEventElement onEvent)
			throws NavigationException {
		for (VariableElement variable : onEvent.getAssociatedScope().getAllVariables()) {
			if (onEvent.getVariableName().equals(variable.getVariableName())) {
				addViolation(variable);
			}
			checkForExplicitFromPartToVariableDuplication(onEvent, variable);
		}
	}

	private void checkForExplicitFromPartToVariableDuplication(
            OnEventElement onEvent, VariableElement variable) {
		try {
			for (FromPartElement fromPart : onEvent.getFromParts()) {
				if (fromPart.getToVariable().equals(variable.getVariableName())) {
					addViolation(variable);
				}
			}
		} catch (NavigationException e) {
			// ignore if no fromPart is available, because then <onEvent>@variable exists
		}
	}

	@Override
	public int getSaNumber() {
		return 86;
	}

}
