package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.VariableElement;
import isabel.model.bpel.mex.ReceiveElement;
import isabel.model.bpel.mex.ReplyElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;

public class SA00058Validator extends Validator {

	public SA00058Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}


	@Override
	public void validate() {
		//FIXME causes errors in two other files
		for (ReceiveElement receive : fileHandler.getAllReceives()) {
			try {
				OperationElement operation = receive.getOperation();
				validateReceive(receive, operation);
			} catch (NavigationException e) {
				// This node could not be validated without a operation
			}
		}
		//FIXME 
		//notWorking();
	}


	private void notWorking() {
		for (ReplyElement reply : fileHandler.getAllReplies()) {
			try {
				OperationElement operation = reply.getOperation();
				validateReply(reply, operation);
			} catch (NavigationException e) {
				// This node could not be validated without a operation
			}
		}
	}

	private void validateReceive(ReceiveElement receive, OperationElement operation) {
		try {
			VariableElement variableForInput = receive.getVariableByName(receive.getVariableAttribute());
			if (!variableForInput.hasCorrespondingMessage(operation.getInput().getMessage(), fileHandler))
				addViolation(receive);
		} catch (NavigationException e) {
			// ignore, if there is no variable attribute. This is validated elsewhere.
		}
	}

	private void validateReply(ReplyElement reply, OperationElement operation) {
		try {
			VariableElement variableForOutput = reply.getVariableByName(reply.getVariableAttribute());
			if (!variableForOutput.hasCorrespondingMessage(operation.getOutput().getMessage(), fileHandler)) {
				addViolation(reply);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable attribute. This is validated elsewhere.
		}
	}

	@Override
	public int getSaNumber() {
		return 58;
	}

}
