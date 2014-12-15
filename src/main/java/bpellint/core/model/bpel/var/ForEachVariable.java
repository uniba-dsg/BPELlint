package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;
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
