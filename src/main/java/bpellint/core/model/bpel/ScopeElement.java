package bpellint.core.model.bpel;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import bpellint.core.model.Standards;
import bpellint.core.model.bpel.fct.CompensateTargetable;
import bpellint.core.model.bpel.flow.SourceElement;
import bpellint.core.model.bpel.flow.TargetElement;
import bpellint.core.model.bpel.var.VariableElement;
import bpellint.core.model.bpel.var.VariablesElement;

import nu.xom.Node;
import nu.xom.Nodes;

public class ScopeElement extends ContainerAwareReferable implements CompensateTargetable {

	private NodeHelper scope;

	public ScopeElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		try {
			scope = new NodeHelper(node, "scope");
		} catch (IllegalArgumentException e) {
			scope = new NodeHelper(node, "process");
		}
	}

	public ScopeElement(Referable scope, ProcessContainer processContainer) {
		this(scope.toXOM(), processContainer);
	}

	public List<ScopeElement> getPeerScopes() {
		List<ScopeElement> peerScopes = new LinkedList<>();
		for (ScopeElement peerScope : getEnclosingScope().getDescendingScopes()) {
			if (this.equals(peerScope)) {
				continue;
			}
			if (getEnclosingScope().equals(peerScope.getEnclosingScope())) {
				peerScopes.add(peerScope);
			}
		}
		return peerScopes;
	}

	private Set<ScopeElement> getDescendingScopes() {
		Set<ScopeElement> descendingScopes = new HashSet<>();
		Nodes query = toXOM().query(".//bpel:scope", Standards.CONTEXT);
		for (Node node : query) {
			descendingScopes.add(new ScopeElement(node, getProcessContainer()));
		}
		return descendingScopes;
	}

	public Set<String> getSourceLinkNames() {
		Set<String> sources = new HashSet<>();
		for (Node node : toXOM().query(".//bpel:source", Standards.CONTEXT)) {
			sources.add(new SourceElement(node, getProcessContainer()).getLinkName());
		}
		return sources;
	}

	public Set<String> getTargetLinkNames() {
		Set<String> targets = new HashSet<>();
		for (Node node : toXOM().query(".//bpel:target", Standards.CONTEXT)) {
			targets.add(new TargetElement(node, getProcessContainer()).getLinkName());
		}
		return targets;
	}

	public List<VariableElement> getAllVariables() throws NavigationException {
		Nodes variables = toXOM().query("./bpel:variables", Standards.CONTEXT);
		if (!variables.hasAny()) {
			throw new NavigationException("<scope> has no <variables> defined");
		}
		return new VariablesElement(variables.get(0), getProcessContainer()).getVariables();
	}

	public boolean isIsolated() {
		return "yes".equals(scope.getAttribute("isolated"));
	}

	public String getNameAttribute() {
		return scope.getAttribute("name");
	}

	public boolean hasExitOnStandardFault() {
		String exitOnStandardFault = scope.getAttribute("exitOnStandardFault");
		if ("yes".equals(exitOnStandardFault)) {
			return true;
		} else if ("no".equals(exitOnStandardFault)) {
			return false;
		} else {
			if ("process".equals(scope.getLocalName())) {
				return false;
			}
			return getEnclosingScope().hasExitOnStandardFault();
		}
	}

}
