package isabel.model.bpel;

import nu.xom.Node;
import isabel.model.NodeHelper;
import isabel.model.Referable;

public class CatchElement implements Referable, VariableLike{

	private NodeHelper catchElement;

	public CatchElement(Node node) {
		catchElement = new NodeHelper(node, "catch");
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
	public Node toXOM() {
		return catchElement.toXOM();
	}

}
