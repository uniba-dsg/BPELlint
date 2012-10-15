package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;

public class OperationHelper {

	private Node operation;

	public OperationHelper(Node operation) {
		super();
		if (operation instanceof Node)
			this.operation = operation;
	}

	public boolean isOneWay() {

		if (operation == null)
			return false;

		return operation.query("child::wsdl:input", CONTEXT).size() == 1
				&& operation.query("child::wsdl:output", CONTEXT).size() == 0;
	}

	public boolean isRequestResponse() {

		if (operation == null)
			return false;

		String firstNodeInputChildQuery = "child::*[position()=1][self::wsdl:input]";
		String secondNodeOutputChildQuery = "child::*[position()=2][self::wsdl:output]";
		return operation.query(firstNodeInputChildQuery, CONTEXT).size() == 1
				&& operation.query(secondNodeOutputChildQuery, CONTEXT).size() == 1;
	}

}
