package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class CompensateElement extends NodeHelper {

    public CompensateElement(Node node) {
        super(node);
    }

    public boolean isWithinFaultHandler() {
        return hasAncestor("bpel:faultHandlers");
    }

    public boolean isWithinCompensationHandler() {
        return hasAncestor("bpel:compensationHandler");
    }

    public boolean isWithinTerminationHandler() {
        return hasAncestor("bpel:terminationHandlers");
    }

}
