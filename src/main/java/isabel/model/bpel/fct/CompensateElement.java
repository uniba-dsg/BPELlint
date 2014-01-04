package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class CompensateElement extends ContainerAwareReferable {

    private final NodeHelper compensate;

	public CompensateElement(Node compensate, ProcessContainer processContainer) {
        super(compensate, processContainer);
        this.compensate = new NodeHelper(compensate, "compensate");
    }

    public boolean isWithinFaultHandler() {
        return compensate.hasAncestor("bpel:faultHandlers");
    }

    public boolean isWithinCompensationHandler() {
        return compensate.hasAncestor("bpel:compensationHandler");
    }

    public boolean isWithinTerminationHandler() {
        return compensate.hasAncestor("bpel:terminationHandlers");
    }

}
