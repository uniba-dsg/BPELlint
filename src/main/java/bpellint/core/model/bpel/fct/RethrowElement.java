package bpellint.core.model.bpel.fct;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
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
