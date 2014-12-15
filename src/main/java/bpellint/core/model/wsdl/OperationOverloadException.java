package bpellint.core.model.wsdl;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;

import nu.xom.Node;
import nu.xom.Nodes;

public class OperationOverloadException extends NavigationException {

	private static final long serialVersionUID = -463433924783013234L;
	private final List<OperationElement> operations;

	public OperationOverloadException(String message, Nodes operationNodes, ProcessContainer processContainer) {
		super(message);
		List<OperationElement> operationList = new LinkedList<>();
		for (Node operation : operationNodes) {
			operationList.add(new OperationElement(operation, processContainer));
		}
		this.operations = Collections.unmodifiableList(operationList);
	}

	public List<OperationElement> getOperations() {
		return operations;
	}
}
