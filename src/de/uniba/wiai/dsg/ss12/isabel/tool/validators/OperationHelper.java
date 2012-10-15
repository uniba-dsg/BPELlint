package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import nu.xom.Node;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class OperationHelper {

	private Node operation;

	public OperationHelper(Node operation) {
		super();

		if (operation == null) {
			throw new IllegalArgumentException("operation is null");
		}

		this.operation = operation;
	}

	public boolean isOneWay() {
		boolean hasInputChildElement = operation.query("child::wsdl:input", CONTEXT).size() == 1;
		boolean hasNoOutputChildElement = operation.query("child::wsdl:output", CONTEXT).size() == 0;
		return hasInputChildElement && hasNoOutputChildElement;
	}

	public boolean isRequestResponse() {
		String firstNodeInputChildQuery = "child::*[position()=1][self::wsdl:input]";
		String secondNodeOutputChildQuery = "child::*[position()=2][self::wsdl:output]";
		return operation.query(firstNodeInputChildQuery, CONTEXT).size() == 1
				&& operation.query(secondNodeOutputChildQuery, CONTEXT).size() == 1;
	}

}
