package bpellint.core.model.wsdl;

import nu.xom.Attribute;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;

import static bpellint.core.model.Standards.CONTEXT;

public class PortTypeElement extends ContainerAwareReferable {

    private final NodeHelper portType;

	public PortTypeElement(Node portType, ProcessContainer processContainer) {
        super(portType, processContainer);
        this.portType = new NodeHelper(portType, "portType");
    }

    public OperationElement getOperationByName(String operationName)
            throws NavigationException, OperationOverloadException {
        Nodes operations = toXOM().query("child::wsdl:operation[attribute::name='" + operationName + "']", CONTEXT);
        if (!operations.hasAny()) {
        	throw new NavigationException("Operation not defined");
        }
		if (!(operations.size() == 1)) {
			throw new OperationOverloadException("Operation name overloaded", operations, getProcessContainer());
		}

        return new OperationElement(operations.get(0), getProcessContainer());
    }

    public String getName() {
        return portType.getAttribute("name");
    }

    public List<Attribute> getOperationNames() {
        List<Attribute> result = new LinkedList<>();

        for(Node node : toXOM().query("child::wsdl:operation/@name", CONTEXT)){
            result.add((Attribute) node);
        }

        return result;
    }
}
