package isabel.model.wsdl;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class PropertyElement extends ContainerAwareReferable {

	private final NodeHelper property;

	public PropertyElement(Node property, ProcessContainer processContainer) {
		super(property, processContainer);
		this.property = new NodeHelper(property, "property");
	}

    public boolean hasElement() {
        return property.hasAttribute("element");
    }

    public boolean hasType() {
        return property.hasAttribute("type");
    }

    public boolean hasNeitherTypeNorElement() {
        return !hasType() && !hasElement();
    }

    public boolean hasTypeAndElement() {
        return hasType() && hasElement();
    }
    
    public String toIdentifier() {
		String targetNamespace = property.getTargetNamespace();
		String name = property.getAttribute("name");
		return "{" + targetNamespace + "}" + name;
	}

	public String getTypeAttribute() {
		return property.getAttribute("type");
	}
    
}
