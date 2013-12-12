package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;

import nu.xom.Node;

public class LinkElement implements Referable {

	private NodeHelper link;

	public LinkElement(Node node) {
		link = new NodeHelper(node, "link");
	}
	
	public String getAttributeName() {
		return link.getAttribute("name");
	}
	
	public SourceElement getSourceElement() {
		return getFlow().getSourceElement(getAttributeName());
	}
	
	public TargetElement getTargetElement() {
		return getFlow().getTargetElement(getAttributeName());
	}

	public FlowElement getFlow() {
		return new FlowElement(link.getParent().getParent());
	}

	@Override
	public Node toXOM() {
		return link.asElement();
	}

}
