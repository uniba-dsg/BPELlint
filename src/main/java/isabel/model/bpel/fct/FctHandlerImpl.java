package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.ScopeElement;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;

public class FctHandlerImpl extends ContainerAwareReferable implements FctHandler {

	public FctHandlerImpl(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		List<ScopeElement> rootScopes = new LinkedList<>();
		for (Node node : toXOM().query(".//bpel:scope", Standards.CONTEXT)) {
			new ScopeElement(node, getProcessContainer());
			if (isRootScope(node)) {
				rootScopes.add(new ScopeElement(node, getProcessContainer()));
			}
		}
		return rootScopes;
	}

	private boolean isRootScope(Node scope) {
		return isDirectHandlerChild(scope.getParent());
	}

	private boolean isDirectHandlerChild(Node node) {
		if (this.equals(node)) {
			return true;
		} else if ("scope".equals(new NodeHelper(node).getLocalName())) {
			return false;
		} else {
			return isDirectHandlerChild(node.getParent());
		}
	}

}
