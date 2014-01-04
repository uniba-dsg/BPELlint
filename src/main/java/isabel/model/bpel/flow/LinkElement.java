package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class LinkElement extends ContainerAwareReferable {

	private final NodeHelper link;

	public LinkElement(Node link, ProcessContainer processContainer) {
		super(link, processContainer);
		this.link = new NodeHelper(link, "link");
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
		return new FlowElement(link.getParent().getParent(), getProcessContainer());
	}

}
