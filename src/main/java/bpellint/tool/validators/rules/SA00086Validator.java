package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.model.bpel.var.FromPartElement;
import bpellint.model.bpel.var.VariableElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00086Validator extends Validator {

	public SA00086Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
