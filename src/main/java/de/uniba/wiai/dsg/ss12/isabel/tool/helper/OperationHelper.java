package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

import nu.xom.Node;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class OperationHelper extends NodeHelper {

	public OperationHelper(Node operation) {
		super(operation);
	}

	public boolean isOneWay() {
		boolean hasInputChildElement = node.query("child::wsdl:input", CONTEXT)
				.size() == 1;
		boolean hasNoOutputChildElement = node.query("child::wsdl:output",
				CONTEXT).size() == 0;
		return hasInputChildElement && hasNoOutputChildElement;
	}

	public boolean isRequestResponse() {
		String firstNodeInputChildQuery = "child::*[position()=1][self::wsdl:input]";
		String secondNodeOutputChildQuery = "child::*[position()=2][self::wsdl:output]";
		return node.query(firstNodeInputChildQuery, CONTEXT).size() == 1
				&& node.query(secondNodeOutputChildQuery, CONTEXT).size() == 1;
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
