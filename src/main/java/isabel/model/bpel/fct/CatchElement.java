package isabel.model.bpel.fct;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.var.VariableLike;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;

import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import nu.xom.Node;

public class CatchElement extends ContainerAwareReferable implements VariableLike, FctHandler {

	private final NodeHelper catchElement;
	private final FctHandlerImpl fctHandlerImpl;

	public CatchElement(Node catchNode, ProcessContainer processContainer) {
		super(catchNode, processContainer);
		this.catchElement = new NodeHelper(catchNode, "catch");
		this.fctHandlerImpl = new FctHandlerImpl(toXOM(), getProcessContainer());
	}

	public boolean hasFaultVariable() {
		return catchElement.hasAttribute("faultVariable");
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

	public boolean hasFaultNameAttribute() {
		return catchElement.hasAttribute("faultName");
	}

	public String getFaultNameAttribute() {
		return catchElement.getAttribute("faultName");
	}

	@Override
	public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		//TODO implement
		throw new NotImplementedException();
	}
}
