package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import nu.xom.Node;

public class PickElement extends ContainerAwareReferable {

	private final NodeHelper pick;

	public PickElement(Node pick, ProcessContainer processContainer) {
		super(pick, processContainer);
		this.pick = new NodeHelper(pick,"pick");
	}

	public PickElement(Referable pick, ProcessContainer processContainer) {
		this(pick.toXOM(), processContainer);
	}

	public boolean isStartActivity() {
		return pick.hasAttribute("createInstance") && "yes".equals(pick.getAttribute("createInstance"));
	}

}
