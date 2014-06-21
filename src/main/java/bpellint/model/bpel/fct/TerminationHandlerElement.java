package bpellint.model.bpel.fct;


import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.ScopeElement;

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
