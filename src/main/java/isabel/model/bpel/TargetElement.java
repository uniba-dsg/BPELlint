package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;
import nu.xom.Node;
import nu.xom.Nodes;

public class TargetElement implements Referable {

	private NodeHelper target;

	public TargetElement(Node node) {
		target = new NodeHelper(node, "target");
	}

	public LinkedActivity getActivity() {
		return new LinkedActivityImpl(target.getParent().getParent());
	}

	public String getLinkName() {
		return target.getAttribute("linkName");
	}

	public LinkElement getLink() {
		return new LinkElement(getFlowNode(target.toXOM()));
	}

	private Node getFlowNode(Node node) {
		NodeHelper nodeHelper = new NodeHelper(node);
		if ("process".equals(nodeHelper.getLocalName())) {
			throw new IllegalStateException("Seems this target has no corresponding link");
		}
		
		if ("flow".equals(nodeHelper.getLocalName())) {
			Nodes link = node.query("./bpel:links/bpel:link[@name='" + getLinkName() + "']", Standards.CONTEXT);
			if (link.hasAny()) {
				return link.get(0);
			}
		}
		
		return getFlowNode(node.getParent());
	}

	@Override
	public Node toXOM() {
		return target.toXOM();
	}

}
