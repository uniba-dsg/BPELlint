package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.fct.FctHandler;
import isabel.tool.impl.ValidationCollector;

import java.util.List;

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
