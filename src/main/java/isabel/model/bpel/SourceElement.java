package isabel.model.bpel;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

public class SourceElement implements Referable, LinkEntity {

	private NodeHelper source;

	public SourceElement(Node node) {
		source = new NodeHelper(node, "source");
	}
	
	public LinkedActivity getActivity() {
		return new LinkedActivityImpl(source.getParent().getParent());
	}
	
	public String getLinkName() {
		return source.getAttribute("linkName");
	}

	public LinkElement getLink() throws NavigationException {
		return new LinkElement(getLinkNode(source.toXOM()));
	}

	private Node getLinkNode(Node node) throws NavigationException {
		NodeHelper nodeHelper = new NodeHelper(node);
		if ("process".equals(nodeHelper.getLocalName())) {
			throw new NavigationException("Seems this target has no corresponding link");
		}
		
		if ("flow".equals(nodeHelper.getLocalName())) {
			Nodes link = node.query("./bpel:links/bpel:link[@name='" + getLinkName() + "']", Standards.CONTEXT);
			if (link.hasAny()) {
				return link.get(0);
			}
		}
		
		return getLinkNode(node.getParent());
	}
	
	@Override
	public Node toXOM() {
		return source.toXOM();
	}

}
