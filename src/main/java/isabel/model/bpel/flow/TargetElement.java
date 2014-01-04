package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import nu.xom.Node;
import nu.xom.Nodes;

public class TargetElement extends ContainerAwareReferable implements LinkEntity {

	private final NodeHelper target;

	public TargetElement(Node target, ProcessContainer processContainer) {
		super(target, processContainer);
		this.target = new NodeHelper(target, "target");
	}

	public LinkedActivity getActivity() {
		return new LinkedActivityImpl(target.getParent().getParent(), getProcessContainer());
	}

	@Override
	public String getLinkName() {
		return target.getAttribute("linkName");
	}

	@Override
	public LinkElement getLink() throws NavigationException {
		return new LinkElement(getLinkNode(target), getProcessContainer());
	}

	private Node getLinkNode(NodeHelper target) throws NavigationException {
		NodeHelper nodeHelper = new NodeHelper(target);
		if ("process".equals(nodeHelper.getLocalName())) {
			throw new NavigationException("Seems this target has no corresponding link");
		}

		if ("flow".equals(nodeHelper.getLocalName())) {
			Nodes link = target.toXOM().query("./bpel:links/bpel:link[@name='" + getLinkName() + "']", Standards.CONTEXT);
			if (link.hasAny()) {
				return link.get(0);
			}
		}

		return getLinkNode(target.getParent());
	}

}
