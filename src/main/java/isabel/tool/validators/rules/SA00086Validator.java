package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.OnEventElement;
import isabel.model.bpel.var.FromPartElement;
import isabel.model.bpel.var.VariableElement;
import isabel.tool.impl.ValidationCollector;

public class SA00086Validator extends Validator {

	public SA00086Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : fileHandler.getAllOnEvents()) {
			try {
				checkForExplicitVariableDoublication(onEvent);
			} catch (NavigationException e) {
				// ignore, no variable => no explicit variable definition
			}
		}
	}

	private void checkForExplicitVariableDoublication(OnEventElement onEvent)
			throws NavigationException {
		for (VariableElement variable : onEvent.getAsociatedScope().getAllVariables()) {
			if (onEvent.getVariableName().equals(variable.getVariableName())) {
				addViolation(onEvent);
			}
			checkForExplicitFromPartToVariableDoublication(onEvent, variable);
		}
	}

	private void checkForExplicitFromPartToVariableDoublication(
			OnEventElement onEvent, VariableElement variable) {
		try {
			for (FromPartElement fromPart : onEvent.getFromParts()) {
				if (fromPart.getToVariable().equals(variable.getVariableName())) {
					addViolation(onEvent);
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
