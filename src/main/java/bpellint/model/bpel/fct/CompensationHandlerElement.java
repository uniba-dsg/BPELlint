package bpellint.model.bpel.fct;


import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.ScopeElement;

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
