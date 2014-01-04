package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
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
