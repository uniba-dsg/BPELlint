package bpellint.core.model.bpel.var;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;
import nu.xom.Node;

public class VariableLikeImpl extends ContainerAwareReferable implements VariableLike {

	private final NodeHelper variableLike;

	public VariableLikeImpl(Node variableLike, ProcessContainer processContainer) {
		super(variableLike, processContainer);
		this.variableLike = new NodeHelper(variableLike);
	}

	@Override
    public boolean hasVariableMessageType() {
        return variableLike.hasAttribute("messageType");
    }

    @Override
    public boolean hasVariableElement() {
        return variableLike.hasAttribute("element");
    }

	@Override
	public String getVariableElement() {
		return variableLike.getAttribute("element");
	}

	@Override
    public String getVariableName() {
        return variableLike.getAttribute("name");
    }

    public String getType() {
        return variableLike.getAttribute("type");
    }

    @Override
    public String getVariableMessageType() {
        return variableLike.getAttribute("messageType");
    }

	@Override
	public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		for (PropertyAliasElement propertyAlias : getProcessContainer().getAllPropertyAliases()) {
			if (propertyAlias.getProperty().equals(property)) {
				if (isEqual(getVariableMessageType(), propertyAlias.getMessageTypeAttribute())) {
					return propertyAlias;
				} else if (isEqual(getVariableElement(), propertyAlias.getElementAttribute())) {
					return propertyAlias;
				} else if (isEqual(getType(), propertyAlias.getTypeAttribute())) {
					return propertyAlias;
				} 
			}
		}

		throw new NavigationException("This variable has no propertyAlias.");
	}

	private boolean isEqual(String qName1, String qName2) {
		return PrefixHelper.removePrefix(qName1).equals(PrefixHelper.removePrefix(qName2)) && !qName1.isEmpty();
	}

}
