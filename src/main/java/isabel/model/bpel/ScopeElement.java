package isabel.model.bpel;

import isabel.model.ComparableNode;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;
import isabel.model.bpel.fct.CompensateTarget;
import isabel.model.bpel.flow.SourceElement;
import isabel.model.bpel.flow.TargetElement;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;

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

	public ScopeElement getEnclosingScope() {
		return scope.getEnclosingScope();
	}

	public List<ScopeElement> getPeerScopes() {
		List<ScopeElement> peerScopes = new LinkedList<>();
		for (ComparableNode comparableNode : scope.getEnclosingScope().getDescendingScopes()) {
			if (!comparableNode.equals(new ComparableNode(toXOM()))) {
				peerScopes.add(new ScopeElement(comparableNode.toXOM()));
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
			sources.add(new SourceElement(node).getLinkName());
		}
		return sources;
	}

	public Set<String> getTargetLinkNames() {
		Set<String> targets = new HashSet<>();
		for (Node node : toXOM().query(".//bpel:target", Standards.CONTEXT)) {
			targets.add(new TargetElement(node).getLinkName());
		}
		return targets;
	}
	
	@Override
	public Node toXOM() {
		return scope.toXOM();
	}

}
