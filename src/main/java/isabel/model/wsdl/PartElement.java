package isabel.model.wsdl;

import nu.xom.Node;
import isabel.model.NodeHelper;
import isabel.model.Referable;

public class PartElement implements Referable{

	private NodeHelper part;
	
	public PartElement(Node node) {
		part = new NodeHelper(node, "part");
	}
	
	public String getElement() {
		return part.getAttribute("element");
	}
	
	@Override
	public Node toXOM() {
		return part.toXOM();
	}

}
