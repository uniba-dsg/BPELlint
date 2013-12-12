package isabel.model.wsdl;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class PropertyElement extends NodeHelper {

	public PropertyElement(Node node) {
		super(node);
	}

    public boolean hasElement() {
        return hasAttribute("element");
    }

    public boolean hasType() {
        return hasAttribute("type");
    }

    public boolean hasNeitherTypeNorElement() {
        return !hasType() && !hasElement();
    }

    public boolean hasTypeAndElement() {
        return hasType() && hasElement();
    }
}
