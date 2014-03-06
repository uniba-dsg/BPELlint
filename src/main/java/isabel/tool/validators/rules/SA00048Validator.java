package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.validators.result.ValidationCollector;

public class SA00048Validator extends Validator {
	
	private static final int INPUT_VARIABLE_EQUALS_OPERATION_MESSAGE_NOT = 1;
	private static final int OUTPUT_VARIABLE_EQUALS_OPERATION_MESSAGE_NOT = 2;

	public SA00048Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

	@Override
	public void validate() {
		for (InvokeElement invoke : processContainer.getAllInvokes()) {
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
			VariableHelper variable = new VariableHelper(processContainer, navigator.getVariableByName(invoke, invoke.getInputVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getInput().getMessage())){
				addViolation(invoke, INPUT_VARIABLE_EQUALS_OPERATION_MESSAGE_NOT);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable. This is validated elsewhere.
		}
	}

	private void validateOutput(InvokeElement invoke, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(processContainer, navigator.getVariableByName(invoke, invoke.getOutputVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getOutput().getMessage())) {
				addViolation(invoke, OUTPUT_VARIABLE_EQUALS_OPERATION_MESSAGE_NOT);
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
