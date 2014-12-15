package bpellint.core.model.wsdl;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;

public class PropertyElement extends ContainerAwareReferable implements Comparable<PropertyElement>{

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

	@Override
	public int compareTo(PropertyElement property) {
		return getName().compareTo(property.getName());
	}

}
