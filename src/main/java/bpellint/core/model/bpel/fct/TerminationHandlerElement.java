package bpellint.core.model.bpel.fct;


import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;

import nu.xom.Node;

public class TerminationHandlerElement extends ContainerAwareReferable implements FctHandler {

	private FctHandlerImpl fctHandlerImpl;

	public TerminationHandlerElement(Node terminationHandler, ProcessContainer processContainer) {
		super(terminationHandler, processContainer);
		new NodeHelper(terminationHandler, "terminationHandler");
		fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

}
