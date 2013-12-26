package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import nu.xom.Node;

public class FaultHandlerContainer extends NodeHelper {
    public FaultHandlerContainer(Node node) {
        super(node);
    }

    public boolean hasCatchAll() {
        return !hasEmptyQueryResult("bpel:catchAll");
    }

    public boolean hasCatches() {
        return !hasEmptyQueryResult("bpel:catch");
    }
}
