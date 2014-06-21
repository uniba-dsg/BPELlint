package bpellint.model.bpel.fct;


import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.ScopeElement;

import nu.xom.Node;

public class CatchAllElement extends ContainerAwareReferable implements FctHandler {

	private final FctHandlerImpl fctHandlerImpl;

	public CatchAllElement(Node catchAll, ProcessContainer processContainer) {
		super(catchAll, processContainer);
		new NodeHelper(catchAll, "catchAll");
		fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}
}
