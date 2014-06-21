package bpellint.model.wsdl;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
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

    public String getName() {
    	return property.getAttribute("name");
    }

	public String getTypeAttribute() {
		return property.getAttribute("type");
	}
    
}
