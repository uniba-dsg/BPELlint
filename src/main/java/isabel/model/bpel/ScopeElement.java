package isabel.model.bpel;

import isabel.model.ComparableNode;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.fct.CompensateTargetable;
import isabel.model.bpel.fct.CompensateTargetableImpl;
import isabel.model.bpel.flow.SourceElement;
import isabel.model.bpel.flow.TargetElement;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;

public class ScopeElement extends ContainerAwareReferable implements CompensateTargetable {

	private final CompensateTargetable compensateTargetDelegate;

	public ScopeElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		try {
			new NodeHelper(node, "scope");
		} catch (IllegalArgumentException e) {
			new NodeHelper(node, "process");
		}
		compensateTargetDelegate = new CompensateTargetableImpl(node, processContainer);
	}

	public ScopeElement(Referable scope, ProcessContainer processContainer) {
		this(scope.toXOM(), processContainer);
	}

	@Override
	public boolean hasCompensationHandler() {
		return compensateTargetDelegate.hasCompensationHandler();
	}

	@Override
	public boolean hasFaultHandler() {
		return compensateTargetDelegate.hasFaultHandler();
	}

	public List<ScopeElement> getPeerScopes() {
		List<ScopeElement> peerScopes = new LinkedList<>();
		for (ComparableNode comparableNode : getEnclosingScope().getDescendingScopes()) {
			if (comparableNode.equals(new ComparableNode(this))) {
				continue;
			}
			ScopeElement peerScope = new ScopeElement(comparableNode.toXOM(), getProcessContainer());
			ComparableNode enclosingScope = new ComparableNode(getEnclosingScope());
			if (new ComparableNode(peerScope.getEnclosingScope()).equals(enclosingScope)) {
				peerScopes.add(peerScope);
			}
		}
		return peerScopes;
	}

	private Set<ComparableNode> getDescendingScopes() {
		Set<ComparableNode> descendingScopes = new HashSet<>();
		Nodes query = toXOM().query(".//bpel:scope", Standards.CONTEXT);
		for (Node node : query) {
			descendingScopes.add(new ComparableNode(node));
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

	@Override
	public Referable getEnclosingFctBarrier() {
		return compensateTargetDelegate.getEnclosingFctBarrier();
	}

}
