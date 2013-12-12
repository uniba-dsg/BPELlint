package isabel.model.bpel;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

public class FlowElement implements Referable {

	private final NodeHelper flow;

	public FlowElement(Node node) {
		flow = new NodeHelper(node, "flow");
	}

	public FlowElement(NodeHelper nodeHelper) {
		this(nodeHelper.toXOM());
	}

	public List<LinkElement> getLinkElements() throws OptionalElementNotPresentException {
		return getLinksElement().getAllLinks();
	}

	private LinksElement getLinksElement()
			throws OptionalElementNotPresentException {
		Nodes links = flow.toXOM().query("./bpel:links", Standards.CONTEXT);
		if(!links.hasAny()){
			throw new OptionalElementNotPresentException("Optional Element <links> not present in <flow>");
		}
		LinksElement linksElement = new LinksElement(links.get(0));
		return linksElement;
	}

	public SourceElement getSourceElement(String linkName) throws NavigationException {
		Node source = getLinkEntity(linkName, "source");
		return new SourceElement(source);
	}

	private Node getLinkEntity(String linkName, String linkEntityType) throws NavigationException {
		Nodes linkEntityNodes = flow.toXOM().query(".//bpel:"+linkEntityType+"[@linkName='" + linkName + "']", Standards.CONTEXT);
		if(!linkEntityNodes.hasAny()){
			throw new NavigationException("Called for <"+linkEntityType+" linkName=\"" + linkName + "\", but it is not in this <flow>");
		}
		
		return linkEntityNodes.get(0);
	}

	@Override
	public Node toXOM() {
		return flow.toXOM();
	}

	public TargetElement getTargetElement(String linkName) throws NavigationException {
		Node target = getLinkEntity(linkName, "target");
		return new TargetElement(target);
	}


}
