package isabel.model.wsdl;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class PropertyAliasElement extends ContainerAwareReferable {

    private final NodeHelper propertyAlias;

	public PropertyAliasElement(Node propertyAlias, ProcessContainer processContainer) {
        super(propertyAlias, processContainer);
        this.propertyAlias = new NodeHelper(propertyAlias, "propertyAlias");
    }

    public PropertyElement getProperty() throws NavigationException {
    	String targetNamespace = PrefixHelper.resolveQNameToNamespace(propertyAlias.toXOM(), getPropertyAttribute());
    	String targetName = PrefixHelper.removePrefix(getPropertyAttribute());
        for (PropertyElement property : getProcessContainer().getAllProperties()) {
        	String name = PrefixHelper.removePrefix(property.getName());
        	String namespace = property.toXOM().getDocument().getRootElement().getAttributeValue("targetNamespace");
            if (targetName.equals(name) && targetNamespace.equals(namespace)) {
                return property;
            }
        }
        throw new NavigationException("Referenced <property> does not exist.");
    }

    private String getPropertyAttribute() {
        return propertyAlias.getAttribute("propertyName");
    }

    public boolean hasMessageType() {
        return propertyAlias.hasAttribute("messageType");
    }

    public boolean hasPart() {
		return propertyAlias.hasAttribute("part");
	}

    public boolean hasType() {
		return propertyAlias.hasAttribute("type");
	}

    public boolean hasElement() {
		return propertyAlias.hasAttribute("element");
	}

    public boolean hasSameType(PropertyAliasElement otherPropertyAlias) {
        return propertyAlias.hasSameAttribute(otherPropertyAlias, "type");
    }

    public boolean hasSameMessageType(PropertyAliasElement otherPropertyAlias) {
        return propertyAlias.hasSameAttribute(otherPropertyAlias, "messageType");
    }

    public boolean hasSameElement(PropertyAliasElement otherPropertyAlias) {
        return propertyAlias.hasSameAttribute(otherPropertyAlias, "element");
    }

    public boolean hasSamePropertyName(PropertyAliasElement otherPropertyAlias) {
        return propertyAlias.hasSameAttribute(otherPropertyAlias, "propertyName");
    }

    public String getPropertyName() {
        return propertyAlias.getAttribute("propertyName");
    }

	public String getElementAttribute() {
		return propertyAlias.getAttribute("element");
	}

	public String getTypeAttribute() {
		return propertyAlias.getAttribute("type");
	}

	public String getMessageTypeAttribute() {
		return propertyAlias.getAttribute("messageType");
	}
	 public String getPart() {
			return propertyAlias.getAttribute("part");
	}

	public String getPropertyNameAttribute() {
		return propertyAlias.getAttribute("propertyName");
	}

	public MessageElement getReferencedMessage() throws NavigationException {
		if (!hasMessageType()) {
			throw new NavigationException("propertyAlias is not referencing a message");
		}

		String ns = PrefixHelper.getPrefix(getMessageTypeAttribute());
		String namespace = propertyAlias.asElement().getNamespaceURI(ns);
		String messageName = PrefixHelper.removePrefix(getMessageTypeAttribute());

		Node message = getProcessContainer().resolveName(namespace, messageName, "wsdl:message");
		return new MessageElement(message, getProcessContainer());
	}
}
