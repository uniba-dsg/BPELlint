package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.bpel.ScopeElement;

import java.util.List;

import nu.xom.Node;

public class CatchAllElement implements FctHandler, Referable {

	private NodeHelper catchAll;
	private FctHandlerImpl fctHandlerImpl;

	public CatchAllElement(Node node) {
		catchAll = new NodeHelper(node, "catchAll");
		fctHandlerImpl = new FctHandlerImpl(toXOM());
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

	@Override
	public Node toXOM() {
		return catchAll.toXOM();
	}
}
