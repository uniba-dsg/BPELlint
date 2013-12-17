package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;

public class SA00048Validator extends Validator {
    public SA00048Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

	@Override
	public void validate() {
		for (InvokeElement invoke : fileHandler.getAllInvokes()) {
			try {
				OperationElement operation = invoke.getOperation();
				validateInput(invoke, operation);
				validateOutput(invoke, operation);
			} catch (NavigationException e) {
				// This node could not be validated without a operation
			}
		}
	}

	private void validateInput(InvokeElement invoke, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(fileHandler, invoke.getVariableByName(invoke.getInputVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getInput().getMessage())){
				addViolation(invoke, 1);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable. This is validated elsewhere.
		}
	}

	private void validateOutput(InvokeElement invoke, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(fileHandler, invoke.getVariableByName(invoke.getOutputVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getOutput().getMessage())) {
				addViolation(invoke, 2);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable. This is validated elsewhere.
		}
	}

    @Override
    public int getSaNumber() {
        return 48;
    }

}
