package bpellint.core.model.bpel.flow;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
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
