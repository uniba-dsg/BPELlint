package isabel.model.wsdl;

import isabel.model.NodeHelper;
import nu.xom.Node;

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
