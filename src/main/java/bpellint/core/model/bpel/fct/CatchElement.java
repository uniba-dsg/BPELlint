package bpellint.core.model.bpel.fct;


import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;
import bpellint.core.model.bpel.var.VariableLike;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;
import nu.xom.Node;

import java.util.List;

public class CatchElement extends ContainerAwareReferable implements VariableLike, FctHandler {

    private final NodeHelper catchElement;
    private final FctHandlerImpl fctHandlerImpl;

    public CatchElement(Node catchNode, ProcessContainer processContainer) {
        super(catchNode, processContainer);
        this.catchElement = new NodeHelper(catchNode, "catch");
        this.fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
    }

    public boolean hasFaultVariable() {
        return catchElement.hasAttribute("faultVariable");
    }

    @Override
    public String getVariableName() {
        return catchElement.getAttribute("faultVariable");
    }

    @Override
    public boolean hasVariableMessageType() {
        return catchElement.hasAttribute("faultMessageType");
    }

    @Override
    public String getVariableMessageType() {
        return catchElement.getAttribute("faultMessageType");
    }

    @Override
    public boolean hasVariableElement() {
        return catchElement.hasAttribute("faultElement");
    }

    @Override
    public String getVariableElement() {
        return catchElement.getAttribute("faultElement");
    }

    @Override
    public List<ScopeElement> getRootScopes() {
        return fctHandlerImpl.getRootScopes();
    }

    public boolean hasFaultNameAttribute() {
        return catchElement.hasAttribute("faultName");
    }

    public String getFaultNameAttribute() {
        return catchElement.getAttribute("faultName");
    }

    @Override
    public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
            throws NavigationException {
        //TODO implement
        throw new UnsupportedOperationException("not yet implemented");
    }
}
