package bpellint.model.bpel.flow;

import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import bpellint.model.bpel.OptionalElementNotPresentException;

import nu.xom.Node;
import nu.xom.Nodes;

public class FlowElement extends ContainerAwareReferable {

	private final NodeHelper flow;

	public FlowElement(Node flow, ProcessContainer processContainer) {
		super(flow, processContainer);
		this.flow = new NodeHelper(flow, "flow");
	}

	public FlowElement(NodeHelper nodeHelper, ProcessContainer processContainer) {
		this(nodeHelper.toXOM(), processContainer);
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
        return new LinksElement(links.get(0), getProcessContainer());
	}

	public SourceElement getSourceElement(String linkName) throws NavigationException {
		Node source = getLinkEntity(linkName, "source");
		return new SourceElement(source, getProcessContainer());
	}

	private Node getLinkEntity(String linkName, String linkEntityType) throws NavigationException {
		Nodes linkEntityNodes = flow.toXOM().query(".//bpel:"+linkEntityType+"[@linkName='" + linkName + "']", Standards.CONTEXT);
		if(!linkEntityNodes.hasAny()){
			throw new NavigationException("Called for <"+linkEntityType+" linkName=\"" + linkName + "\", but it is not in this <flow>");
		}

		return linkEntityNodes.get(0);
	}

	public TargetElement getTargetElement(String linkName) throws NavigationException {
		Node target = getLinkEntity(linkName, "target");
		return new TargetElement(target, getProcessContainer());
	}


}
