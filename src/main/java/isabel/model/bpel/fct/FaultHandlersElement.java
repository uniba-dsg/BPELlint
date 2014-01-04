package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class FaultHandlersElement extends ContainerAwareReferable {

    private final NodeHelper faultHandlers;

	public FaultHandlersElement(Node faultHandlers, ProcessContainer processContainer) {
        super(faultHandlers, processContainer);
        this.faultHandlers = new NodeHelper(faultHandlers, "faultHandlers");
    }

    public boolean hasCatchAll() {
        return !faultHandlers.hasEmptyQueryResult("bpel:catchAll");
    }

    public boolean hasCatches() {
        return !faultHandlers.hasEmptyQueryResult("bpel:catch");
    }
}
