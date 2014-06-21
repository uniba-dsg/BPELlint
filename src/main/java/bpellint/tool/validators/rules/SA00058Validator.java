package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.VariableHelper;
import bpellint.model.bpel.mex.ReceiveElement;
import bpellint.model.bpel.mex.ReplyElement;
import bpellint.model.wsdl.OperationElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00058Validator extends Validator {

	public SA00058Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}


	@Override
	public void validate() {
		for (ReceiveElement receive : processContainer.getAllReceives()) {
			try {
				OperationElement operation = receive.getOperation();
				validateReceive(receive, operation);
			} catch (NavigationException e) {
				// This node could not be validated without a operation
			}
		}
		for (ReplyElement reply : processContainer.getAllReplies()) {
			try {
				OperationElement operation = reply.getOperation();
				if(reply.hasFaultNameAttribute()){
					validateFault(reply, operation);
					continue;
				}
				validateReply(reply, operation);
			} catch (NavigationException e) {
				// This node could not be validated without a operation
			}
		}
	}

	private void validateReceive(ReceiveElement receive, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(processContainer, navigator.getVariableByName(receive, receive.getVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getInput().getMessage())){
				addViolation(receive);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable attribute. This is validated elsewhere.
		}
	}

	private void validateReply(ReplyElement reply, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(processContainer, navigator.getVariableByName(reply, reply.getVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getOutput().getMessage())) {
				addViolation(reply);
			}
		} catch (NavigationException e) {
			// ignore, if there is no variable attribute. This is validated elsewhere.
		}
	}
	
	private void validateFault(ReplyElement reply, OperationElement operation) {
		try {
			VariableHelper variable = new VariableHelper(processContainer, navigator.getVariableByName(reply, reply.getVariableAttribute()));
			if (!variable.hasCorrespondingMessage(operation.getFault().getMessage())) {
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
