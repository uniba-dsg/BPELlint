package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.bpel.ScopeElement;

import java.util.List;

import nu.xom.Node;

public class TerminationHandlerElement implements FctHandler, Referable {

	private NodeHelper terminationHandler;
	private FctHandlerImpl fctHandlerImpl;

	public TerminationHandlerElement(Node node) {
		terminationHandler = new NodeHelper(node, "terminationHandler");
		fctHandlerImpl = new FctHandlerImpl(toXOM());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

	@Override
	public Node toXOM() {
		return terminationHandler.toXOM();
	}

}
