package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.ScopeElement;

import java.util.List;

import nu.xom.Node;

public class CompensationHandlerElement extends ContainerAwareReferable implements FctHandler {

	private FctHandlerImpl fctHandlerImpl;

	public CompensationHandlerElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		new NodeHelper(node, "compensationHandler");
		fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

}
