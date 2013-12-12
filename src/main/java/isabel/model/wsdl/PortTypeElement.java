package isabel.model.wsdl;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class PortTypeElement extends NodeHelper {

    public PortTypeElement(Node node) {
        super(node);
    }

    public OperationElement getOperationByName(String operationName)
            throws NavigationException {
        Nodes operations = toXOM().query("child::wsdl:operation[attribute::name='" + operationName + "']", CONTEXT);
        if (operations.hasAny())
            return new OperationElement(operations.get(0));

        throw new NavigationException("Operation not defined");
    }

    public String getName() {
        return getAttribute("name");
    }
}
