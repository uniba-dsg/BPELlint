package bpellint.model.bpel.flow;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import nu.xom.Node;
import nu.xom.Nodes;

public class LinkEntityImpl extends ContainerAwareReferable implements LinkEntity {

	public LinkEntityImpl(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
	}

	@Override
	public String getLinkName() {
		return new NodeHelper(toXOM()).getAttribute("linkName");
	}

	@Override
	public LinkElement getLink() throws NavigationException {
		return new LinkElement(getLinkNode(toXOM()), getProcessContainer());
	}

	private Node getLinkNode(Node node) throws NavigationException {
		NodeHelper nodeHelper = new NodeHelper(node);
		if ("process".equals(nodeHelper.getLocalName())) {
			throw new NavigationException("Seems this target has no corresponding link");
		}

		if ("flow".equals(nodeHelper.getLocalName())) {
			Nodes link = node.query("./bpel:links/bpel:link[@name='" + getLinkName() + "']",
					Standards.CONTEXT);
			if (link.hasAny()) {
				return link.get(0);
			}
		}

		return getLinkNode(node.getParent());
	}

}
