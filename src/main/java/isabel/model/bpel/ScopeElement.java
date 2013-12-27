package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.fct.CompensateTarget;
import nu.xom.Node;

public class ScopeElement implements CompensateTarget, Referable {

	private NodeHelper scope;

	public ScopeElement(Node node) {
		this(new NodeHelper(node, "scope"));
	}

	public ScopeElement(NodeHelper parent) {
		scope = parent;
	}

	@Override
	public boolean hasCompensationHandler() {
		return toXOM().query("./bpel:compensationHandler", Standards.CONTEXT).hasAny();
	}

	@Override
	public boolean hasFaultHandler() {
		return toXOM().query("./bpel:faultHandlers", Standards.CONTEXT).hasAny();
	}

	@Override
	public Node toXOM() {
		return scope.toXOM();
	}
}
