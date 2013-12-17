package isabel.model.wsdl;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import nu.xom.Attribute;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.LinkedList;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class PortTypeElement extends NodeHelper {

    public PortTypeElement(Node node) {
        super(node);
    }

    public OperationElement getOperationByName(String operationName)
            throws NavigationException {
        Nodes operations = toXOM().query("child::wsdl:operation[attribute::name='" + operationName + "']", CONTEXT);
        if (!operations.hasAny()) {
        	throw new NavigationException("Operation not defined");
        }
        if (!(operations.size() == 1)) {
        	throw new NavigationException("Operation name overloaded");
        }
        
        return new OperationElement(operations.get(0));
    }

    public String getName() {
        return getAttribute("name");
    }

    public List<Attribute> getOperationNames() {
        List<Attribute> result = new LinkedList<>();

        for(Node node : toXOM().query("child::wsdl:operation/@name", CONTEXT)){
            result.add((Attribute) node);
        }

        return result;
    }
}
