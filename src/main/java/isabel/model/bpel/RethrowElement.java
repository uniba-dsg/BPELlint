package isabel.model.bpel;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class RethrowElement extends NodeHelper {

    public RethrowElement(Node node) {
        super(node);
    }

    public boolean isWithinFaultHandler() {
        return hasAncestor("bpel:faultHandlers");
    }
}
