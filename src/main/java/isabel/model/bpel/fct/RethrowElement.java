package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class RethrowElement extends ContainerAwareReferable {

    private final NodeHelper rethrow;

	public RethrowElement(Node rethrow, ProcessContainer processContainer) {
        super(rethrow, processContainer);
        this.rethrow = new NodeHelper(rethrow, "rethrow");
    }

    public boolean isWithinFaultHandler() {
        return rethrow.hasAncestor("bpel:faultHandlers");
    }

}
