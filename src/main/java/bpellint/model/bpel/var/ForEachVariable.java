package bpellint.model.bpel.var;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;
import nu.xom.Node;


public class ForEachVariable extends ContainerAwareReferable implements VariableLike {
    private final NodeHelper forEach;

    public ForEachVariable(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        this.forEach = new NodeHelper(node, "forEach");
    }

    @Override
    public String getVariableName() {
        return forEach.getAttribute("counterName");
    }

    @Override
    public boolean hasVariableMessageType() {
        return false;
    }

    @Override
    public String getVariableMessageType() {
        return "";
    }

    @Override
    public boolean hasVariableElement() {
        return false;
    }

    @Override 
    public String getVariableElement() {
        return "";
    }

    @Override
    public PropertyAliasElement resolvePropertyAlias(PropertyElement property) throws NavigationException {
        throw new NavigationException("A <forEach> counter has no <propertyAlias>");
    }

}
