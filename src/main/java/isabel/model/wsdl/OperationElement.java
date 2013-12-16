package isabel.model.wsdl;

import java.util.LinkedList;
import java.util.List;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import nu.xom.Node;
import nu.xom.Nodes;
import static isabel.model.Standards.CONTEXT;

public class OperationElement extends NodeHelper {

	public OperationElement(Node operation) {
		super(operation);

		if (!getLocalName().equals("operation")) {
			throw new IllegalArgumentException(
					"operation helper only works for wsdl:operation elements");
		}
	}

	public boolean isOneWay() {
		return hasInput() && !hasOutput();
	}

	public boolean isRequestResponse() {
		return hasInput() && hasOutput() && !isFirstChildOutput();
	}

	public boolean isNotification() {
		return hasOutput() && !hasInput();
	}

	public boolean isSolicitResponse() {
		return hasOutput() && hasInput() && isFirstChildOutput();
	}
	
	public List<OperationMessage> getOperationMessages() throws NavigationException {
		List<OperationMessage> messages = new LinkedList<>();
		try {
			messages.add(getInput());
		} catch (NavigationException e) {
			// ignore just collect any OperationMessage
		}
		try {
			messages.add(getOutput());
		} catch (NavigationException e) {
			// ignore just collect any OperationMessage
		}
		try {
			messages.add(getFault());
		} catch (NavigationException e) {
			// ignore just collect any OperationMessage
		}

		if (messages.isEmpty()) {
			throw new NavigationException("Messages are not defined.");
        }
        
        return messages;
    }

	public OperationMessage getInput() throws NavigationException {
		Nodes input = toXOM().query("./wsdl:input", CONTEXT);
		if (!input.hasAny()) {
			throw new NavigationException("<operation> has no input message");
		}

		return new OperationInputElement(input.get(0));
	}
	
	public OperationMessage getOutput() throws NavigationException {
		Nodes output = toXOM().query("./wsdl:output", CONTEXT);
		if (!output.hasAny()) {
			throw new NavigationException("<operation> has no output message");
		}

		return new OperationOutputElement(output.get(0));
	}
	
	public OperationMessage getFault() throws NavigationException {
		Nodes fault = toXOM().query("./wsdl:fault", CONTEXT);
		if (!fault.hasAny()) {
			throw new NavigationException("<operation> has no fault message");
		}

		return new OperationFaultElement(fault.get(0));
	}

	private boolean isFirstChildOutput() {
		Node firstOperationChild = node.query("(child::*)[1]").get(0);
		Node operationOutput = node.query("child::wsdl:output", CONTEXT).get(0);
		return firstOperationChild.equals(operationOutput);
	}

	private boolean hasOutput() {
		return hasQueryResult("child::wsdl:output");
	}

	private boolean hasInput() {
		return hasQueryResult("child::wsdl:input");
	}

}
