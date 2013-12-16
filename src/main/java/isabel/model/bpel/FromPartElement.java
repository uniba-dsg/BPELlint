package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import nu.xom.Node;

public class FromPartElement implements Referable{

	private NodeHelper fromPart;

	public FromPartElement(Node node) {
		fromPart = new NodeHelper(node, "fromPart");
	}

	@Override
	public Node toXOM() {
		return fromPart.toXOM();
	}

	public String getToVariable() {
		return fromPart.getAttribute("toVariable");
	}

}
