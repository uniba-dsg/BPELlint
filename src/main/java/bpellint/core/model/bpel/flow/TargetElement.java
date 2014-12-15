package bpellint.core.model.bpel.flow;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Node;

public class TargetElement extends ContainerAwareReferable implements LinkEntity {

	private final LinkEntityImpl linkEntityDelegate;

	public TargetElement(Node target, ProcessContainer processContainer) {
		super(target, processContainer);
		new NodeHelper(target, "target");
		linkEntityDelegate = new LinkEntityImpl(target, processContainer);
	}

	public LinkedActivity getActivity() {
		return new LinkedActivityImpl(toXOM().getParent().getParent(), getProcessContainer());
	}

	@Override
	public String getLinkName() {
		return linkEntityDelegate.getLinkName();
	}

	@Override
	public LinkElement getLink() throws NavigationException {
		return linkEntityDelegate.getLink();
	}

}
