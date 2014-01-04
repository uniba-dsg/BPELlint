package isabel.model.bpel.flow;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

public class SourceElement extends ContainerAwareReferable implements LinkEntity {

	private final NodeHelper source;

	public SourceElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		source = new NodeHelper(node, "source");
	}

	public LinkedActivity getActivity() {
		return new LinkedActivityImpl(source.getParent().getParent(), getProcessContainer());
	}

	@Override
	public String getLinkName() {
		return source.getAttribute("linkName");
	}

	@Override
	public LinkElement getLink() throws NavigationException {
		return new LinkElement(getLinkNode(source), getProcessContainer());
	}

	private Node getLinkNode(NodeHelper source) throws NavigationException {
		NodeHelper nodeHelper = new NodeHelper(source);
		if ("process".equals(nodeHelper.getLocalName())) {
			throw new NavigationException("Seems this target has no corresponding link");
		}

		if ("flow".equals(nodeHelper.getLocalName())) {
			Nodes link = source.toXOM().query("./bpel:links/bpel:link[@name='" + getLinkName() + "']",
					Standards.CONTEXT);
			if (link.hasAny()) {
				return link.get(0);
			}
		}

		return getLinkNode(source.getParent());
	}

	@Override
	public Node toXOM() {
		return source.toXOM();
	}

}
