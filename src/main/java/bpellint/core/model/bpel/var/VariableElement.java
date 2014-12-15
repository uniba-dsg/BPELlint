package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;
import nu.xom.Node;

public class VariableElement extends ContainerAwareReferable implements VariableLike {

	private NodeHelper variable;
	private VariableLikeImpl variableDelegate;

	public VariableElement(Node variable, ProcessContainer processContainer) {
        super(variable, processContainer);
        this.variable = new NodeHelper(variable,"variable");
        this.variableDelegate = new VariableLikeImpl(variable, processContainer);
    }


    public boolean hasType() {
        return variable.hasAttribute("type");
    }

	@Override
    public boolean hasVariableMessageType() {
        return variableDelegate.hasVariableMessageType();
    }

    @Override
    public boolean hasVariableElement() {
        return variableDelegate.hasVariableElement();
    }

	@Override
	public String getVariableElement() {
		return variableDelegate.getVariableElement();
	}

	@Override
    public String getVariableName() {
        return variableDelegate.getVariableName();
    }

    public String getType() {
        return variableDelegate.getType();
    }

    @Override
    public String getVariableMessageType() {
        return variableDelegate.getVariableMessageType();
    }


	@Override
	public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		return variableDelegate.resolvePropertyAlias(property);
	}

}
