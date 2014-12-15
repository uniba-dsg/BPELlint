package bpellint.core.model.bpel.fct;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;
import bpellint.core.model.bpel.ScopeElement;
import bpellint.core.model.bpel.mex.InvokeElement;
import nu.xom.Node;
import nu.xom.Nodes;

public class CompensateScopeElement extends ContainerAwareReferable {

	private final NodeHelper compensateScope;

	public CompensateScopeElement(Node compensateScope, ProcessContainer processContainer) {
		super(compensateScope, processContainer);
		this.compensateScope = new NodeHelper(compensateScope, "compensateScope");
	}

	public boolean isWithinFaultHandler() {
		return compensateScope.hasAncestor("bpel:faultHandlers");
	}

	public boolean isWithinCompensationHandler() {
		return compensateScope.hasAncestor("bpel:compensationHandler");
	}

	public boolean isWithinTerminationHandler() {
		return compensateScope.hasAncestor("bpel:terminationHandler");
	}

	public String getTargetAttribute() {
		return compensateScope.getAttribute("target");
	}

	public CompensateTargetable getTarget() throws NavigationException {
		ScopeElement parentScope = getEnclosingScope();
		Nodes targetScope = parentScope.toXOM().query(".//bpel:scope[@name='" + getTargetAttribute() + "']", Standards.CONTEXT);
		if (targetScope.hasAny()) {
			return new ScopeElement(targetScope.get(0), getProcessContainer());
		}
		Nodes targetInvoke = parentScope.toXOM().query(".//bpel:invoke[@name='" + getTargetAttribute() + "']", Standards.CONTEXT);
		if (targetInvoke.hasAny()) {
			return new InvokeElement(targetInvoke.get(0), getProcessContainer());
		}
		throw new NavigationException("Target is not in enclosing <scope>");
	}

}
