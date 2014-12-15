package bpellint.core.validators.rules;


import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;
import bpellint.core.model.bpel.fct.FctHandler;

public class SA00079Validator extends Validator {

	public SA00079Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (FctHandler fctHandler : processContainer.getAllFctHandler()) {
			List<ScopeElement> rootScopes = fctHandler.getRootScopes();
			for (ScopeElement scope : rootScopes) {
				if (scope.hasCompensationHandler()) {
					addViolation(fctHandler);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 79;
	}

}
