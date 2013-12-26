package isabel.model.bpel.fct;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.VariableLike;

import java.util.List;

import nu.xom.Node;

public class CatchElement implements VariableLike, FctHandler, Referable {

	private NodeHelper catchElement;
	private FctHandlerImpl fctHandlerImpl;

	public CatchElement(Node node) {
		catchElement = new NodeHelper(node, "catch");
		fctHandlerImpl = new FctHandlerImpl(toXOM());
	}

	@Override
	public String getVariableName() {
		return catchElement.getAttribute("faultVariable");
	}

	@Override
	public boolean hasVariableMessageType() {
		return catchElement.hasAttribute("faultMessageType");
	}

	@Override
	public String getVariableMessageType() {
		return catchElement.getAttribute("faultMessageType");
	}

	@Override
	public boolean hasVariableElement() {
		return catchElement.hasAttribute("faultElement");
	}

	@Override
	public String getVariableElement() {
		return catchElement.getAttribute("faultElement");
	}

	@Override
	public List<ScopeElement> getRootScopes() {
		return fctHandlerImpl.getRootScopes();
	}

	@Override
	public Node toXOM() {
		return catchElement.toXOM();
	}

}
