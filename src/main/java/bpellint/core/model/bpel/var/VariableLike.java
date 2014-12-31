package bpellint.core.model.bpel.var;

import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;

public interface VariableLike extends Referable {

    default boolean hasVariableMessageType() {
        return getVariableLikeNodeHelper().hasAttribute("messageType");
    }

    default boolean hasVariableElement() {
        return getVariableLikeNodeHelper().hasAttribute("element");
    }

    default String getVariableElement() {
		return getVariableLikeNodeHelper().getAttribute("element");
	}

    default String getVariableName() {
        return getVariableLikeNodeHelper().getAttribute("name");
    }

    default String getVariableType() {
        return getVariableLikeNodeHelper().getAttribute("type");
    }

    default String getVariableMessageType() {
        return getVariableLikeNodeHelper().getAttribute("messageType");
    }

    default PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		for (PropertyAliasElement propertyAlias : getProcessContainer().getAllPropertyAliases()) {
			if (propertyAlias.getProperty().equals(property)) {
				if (isEqual(getVariableMessageType(), propertyAlias.getMessageTypeAttribute())) {
					return propertyAlias;
				} else if (isEqual(getVariableElement(), propertyAlias.getElementAttribute())) {
					return propertyAlias;
				} else if (isEqual(getVariableType(), propertyAlias.getTypeAttribute())) {
					return propertyAlias;
				} 
			}
		}

		throw new NavigationException("This variable has no propertyAlias.");
	}

	static boolean isEqual(String qName1, String qName2) {
		return PrefixHelper.removePrefix(qName1).equals(PrefixHelper.removePrefix(qName2)) && !qName1.isEmpty();
	}

	default NodeHelper getVariableLikeNodeHelper(){
		return new NodeHelper(toXOM());
	}

	ProcessContainer getProcessContainer();
}
