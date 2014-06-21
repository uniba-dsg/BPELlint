package bpellint.model.bpel.flow;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
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
