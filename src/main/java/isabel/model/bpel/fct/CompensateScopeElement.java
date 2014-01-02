package isabel.model.bpel.fct;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.mex.InvokeElement;
import nu.xom.Node;
import nu.xom.Nodes;

public class CompensateScopeElement implements Referable {

	private final NodeHelper compensateScope;
	private final ProcessContainer processContainer;

	public CompensateScopeElement(Node node, ProcessContainer processContainer) {
		compensateScope = new NodeHelper(node, "compensateScope");
		this.processContainer = processContainer;
	}

	public boolean isWithinFaultHandler() {
		return compensateScope.hasAncestor("bpel:faultHandlers");
	}

	public boolean isWithinCompensationHandler() {
		return compensateScope.hasAncestor("bpel:compensationHandler");
	}

	public boolean isWithinTerminationHandler() {
		return compensateScope.hasAncestor("bpel:terminationHandlers");
	}

	public String getTargetAttribute() {
		return compensateScope.getAttribute("target");
	}

	public CompensateTarget getTarget() throws NavigationException {
		ScopeElement parentScope = compensateScope.getEnclosingScope();
		Nodes targetScope = parentScope.toXOM().query(".//bpel:scope[@name='" + getTargetAttribute() + "']", Standards.CONTEXT);
		if (targetScope.hasAny()) {
			return new ScopeElement(targetScope.get(0));
		}
		Nodes targetInvoke = parentScope.toXOM().query(".//bpel:invoke[@name='" + getTargetAttribute() + "']", Standards.CONTEXT);
		if (targetInvoke.hasAny()) {
			return new InvokeElement(targetInvoke.get(0), processContainer);
		}
		throw new NavigationException("Target is not in enclosing <scope>");
	}

	public ScopeElement getEnclosingScope() {
		return compensateScope.getEnclosingScope();
	}

	@Override
	public Node toXOM() {
		return compensateScope.toXOM();
	}

}
