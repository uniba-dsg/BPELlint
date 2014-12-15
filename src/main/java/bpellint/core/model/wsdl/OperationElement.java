package bpellint.core.model.wsdl;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import static bpellint.core.model.Standards.CONTEXT;

public class OperationElement extends ContainerAwareReferable {

	private final NodeHelper operation;

	public OperationElement(Node operation, ProcessContainer processContainer) {
		super(operation, processContainer);
		this.operation = new NodeHelper(operation, "operation");
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

	public OperationMessage getInput() throws NavigationException {
		Nodes input = toXOM().query("./wsdl:input", CONTEXT);
		if (!input.hasAny()) {
			throw new NavigationException("<operation> has no input message");
		}

		return new OperationInputElement(input.get(0), getProcessContainer());
	}

	public OperationMessage getOutput() throws NavigationException {
		Nodes output = toXOM().query("./wsdl:output", CONTEXT);
		if (!output.hasAny()) {
			throw new NavigationException("<operation> has no output message");
		}

		return new OperationOutputElement(output.get(0), getProcessContainer());
	}

	public OperationMessage getFault() throws NavigationException {
		Nodes fault = toXOM().query("./wsdl:fault", CONTEXT);
		if (!fault.hasAny()) {
			throw new NavigationException("<operation> has no fault message");
		}

		return new OperationFaultElement(fault.get(0), getProcessContainer());
	}

	public PortTypeElement getPortType() throws NavigationException {
		Nodes portType = toXOM().query("./..", CONTEXT);
		try {
			PortTypeElement portTypeElement = new PortTypeElement(portType.get(0), getProcessContainer());
			return portTypeElement;
		} catch (IllegalArgumentException e) {
			throw new NavigationException("<portType> can not be found");
		}
	}

	private boolean isFirstChildOutput() {
		Node firstOperationChild = toXOM().query("(child::*)[1]").get(0);
		Node operationOutput = toXOM().query("child::wsdl:output", CONTEXT).get(0);
		return firstOperationChild.equals(operationOutput);
	}

	private boolean hasOutput() {
		return operation.hasQueryResult("child::wsdl:output");
	}

	private boolean hasInput() {
		return operation.hasQueryResult("child::wsdl:input");
	}

}
