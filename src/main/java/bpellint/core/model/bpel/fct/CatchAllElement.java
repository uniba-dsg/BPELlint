package bpellint.core.model.bpel.fct;


import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;

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
