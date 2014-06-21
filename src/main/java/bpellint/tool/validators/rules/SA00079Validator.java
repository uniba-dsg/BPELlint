package bpellint.tool.validators.rules;


import java.util.List;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.ScopeElement;
import bpellint.model.bpel.fct.FctHandler;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00079Validator extends Validator {

	public SA00079Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
