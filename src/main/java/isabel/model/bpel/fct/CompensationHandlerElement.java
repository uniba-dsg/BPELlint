package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.ScopeElement;

import java.util.List;

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
