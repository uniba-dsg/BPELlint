package bpellint.model.bpel.flow;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import nu.xom.Node;

public class SourceElement extends ContainerAwareReferable implements LinkEntity {

	private final LinkEntityImpl linkEntityDelegate;

	public SourceElement(Node source, ProcessContainer processContainer) {
		super(source, processContainer);
		new NodeHelper(source, "source");
		linkEntityDelegate = new LinkEntityImpl(source, processContainer);
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
