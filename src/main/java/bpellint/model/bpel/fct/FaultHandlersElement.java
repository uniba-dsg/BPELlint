package bpellint.model.bpel.fct;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

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

	public List<CatchElement> getCatches() throws NavigationException {
		if (!hasCatches()) {
			throw new NavigationException("Has no <catch>.");
		}
		LinkedList<CatchElement> catchElements = new LinkedList<>();
		for (Node catchNode : toXOM().query("./bpel:catch", Standards.CONTEXT)) {
			catchElements.add(new CatchElement(catchNode, getProcessContainer()));
		}
		return catchElements;
	}
}
