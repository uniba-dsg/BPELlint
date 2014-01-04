package isabel.model.bpel.flow;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

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
