package bpellint.core.model.bpel.fct;


import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.ScopeElement;

import nu.xom.Node;

public class CompensationHandlerElement extends ContainerAwareReferable implements FctHandler {

	private final FctHandlerImpl fctHandlerImpl;

	public CompensationHandlerElement(Node compensationHandler, ProcessContainer processContainer) {
		super(compensationHandler, processContainer);
		new NodeHelper(compensationHandler, "compensationHandler");
		fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

}
