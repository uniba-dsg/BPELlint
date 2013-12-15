package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import nu.xom.Node;

public class PickElement implements Referable {

	private NodeHelper pick;

	public PickElement(Node node) {
		this.pick = new NodeHelper(node,"pick");
	}
	
	
	public PickElement(NodeHelper nodeHelper) {
		this(nodeHelper.toXOM());
	}


	@Override
	public Node toXOM() {
		return pick.toXOM();
	}

	public boolean isStartActivity() {
		return pick.hasAttribute("createInstance") && "yes".equals(pick.getAttribute("createInstance"));
	}

}
