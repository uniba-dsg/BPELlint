package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.bpel.ScopeElement;

import java.util.List;

import nu.xom.Node;

public class CompensationHandlerElement implements FctHandler, Referable {

	private NodeHelper comensationHandler;
	private FctHandlerImpl fctHandlerImpl;

	public CompensationHandlerElement(Node node) {
		comensationHandler = new NodeHelper(node, "compensationHandler");
		fctHandlerImpl = new FctHandlerImpl(toXOM());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

	@Override
	public Node toXOM() {
		return comensationHandler.toXOM();
	}

}
