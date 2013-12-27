package isabel.model.bpel.flow;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import nu.xom.Node;

public class LinkElement implements Referable {

	private NodeHelper link;

	public LinkElement(Node node) {
		link = new NodeHelper(node, "link");
	}
	
	public String getName() {
		return link.getAttribute("name");
	}
	
	public SourceElement getSourceElement() throws NavigationException {
		return getFlow().getSourceElement(getName());
	}
	
	public TargetElement getTargetElement() throws NavigationException {
		return getFlow().getTargetElement(getName());
	}

	public FlowElement getFlow() {
		return new FlowElement(link.getParent().getParent());
	}

	@Override
	public Node toXOM() {
		return link.asElement();
	}

}
