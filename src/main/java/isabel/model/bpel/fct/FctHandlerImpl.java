package isabel.model.bpel.fct;

import isabel.model.ComparableNode;
import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.model.bpel.ScopeElement;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;

public class FctHandlerImpl implements FctHandler {

	private Node node;

	public FctHandlerImpl(Node node) {
		this.node = node;
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		List<ScopeElement> rootScopes = new LinkedList<>();
		for (Node node : toXOM().query(".//bpel:scope", Standards.CONTEXT)) {
			if (isRootScope(node)) {
				rootScopes.add(new ScopeElement(node));
			}
		}
		return rootScopes;
	}

	private boolean isRootScope(Node scope) {
		return isDirectHandlerChild(scope.getParent());
	}

	private boolean isDirectHandlerChild(Node node) {
		ComparableNode compensationHandler = new ComparableNode(toXOM());
		if (compensationHandler.equals(new ComparableNode(node))) {
			return true;
		} else if ("scope".equals(new NodeHelper(node).getLocalName())) {
			return false;
		} else {
			return isDirectHandlerChild(node.getParent());
		}
	}

	@Override
	public Node toXOM() {
		return node;
	}

}
