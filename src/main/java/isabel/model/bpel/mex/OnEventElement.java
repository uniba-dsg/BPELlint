package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.ScopeElement;
import isabel.model.bpel.var.FromPartElement;
import isabel.model.bpel.var.FromPartsElement;
import isabel.model.bpel.var.VariableLike;
import isabel.model.bpel.var.VariableLikeImpl;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;
import nu.xom.Nodes;

public class OnEventElement extends ContainerAwareReferable implements
		MessageActivity, VariableLike {

	private final NodeHelper onEvent;
	private final MessageActivity delegate;
	private final VariableLikeImpl variableDelegate;

	public OnEventElement(Node onEvent, ProcessContainer processContainer) {
		super(onEvent, processContainer);
		this.onEvent = new NodeHelper(onEvent, "onEvent");
		this.delegate = new MessageActivityImpl(this, processContainer);
		this.variableDelegate = new VariableLikeImpl(onEvent, processContainer);
	}

	@Override
	public Type getType() {
		return delegate.getType();
	}

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		return delegate.getPartnerLink();
	}

	@Override
	public PortTypeElement getPortType() throws NavigationException {
		return delegate.getPortType();
	}

	@Override
	public OperationElement getOperation() throws NavigationException {
		return delegate.getOperation();
	}

	@Override
	public List<CorrelationElement> getCorrelations()
			throws NavigationException {
		return delegate.getCorrelations();
	}

	@Override
	public String getPartnerLinkAttribute() {
		return delegate.getPartnerLinkAttribute();
	}

	@Override
	public String getOperationAttribute() {
		return delegate.getOperationAttribute();
	}

	@Override
	public String getPortTypeAttribute() {
		return delegate.getPortTypeAttribute();
	}

	@Override
	public boolean isReceiving() {
		return delegate.isReceiving();
	}

	@Override
	public String getMessageExchangeAttribute() {
		return delegate.getMessageExchangeAttribute();
	}
	
	@Override
    public boolean hasVariableMessageType() {
        return variableDelegate.hasVariableMessageType();
    }
	
    @Override
    public boolean hasVariableElement() {
        return variableDelegate.hasVariableElement();
    }

	@Override
	public String getVariableElement() {
		return variableDelegate.getVariableElement();
	}
    
    @Override
    public String getVariableMessageType() {
        return variableDelegate.getVariableMessageType();
    }

	@Override
	public String getVariableName() {
		return onEvent.getAttribute("variable");
	}

	public boolean hasVariableAttribute() {
		return !"".equals(getVariableName());
	}

	public ScopeElement getAsociatedScope() {
		Node scope = toXOM().query("./bpel:scope", Standards.CONTEXT).get(0);
		return new ScopeElement(scope, getProcessContainer());
	}

	public boolean hasFromParts() {
		try {
			getFromParts();
			return true;
		} catch (NavigationException e) {
			return false;
		}
	}

	public List<FromPartElement> getFromParts() throws NavigationException {
		Nodes fromParts = toXOM().query("./bpel:fromParts", Standards.CONTEXT);
		if (!fromParts.hasAny()) {
			throw new NavigationException("<onEvent> has no <fromParts>");
		}
		return new FromPartsElement(fromParts.get(0), getProcessContainer()).getAllFromParts();
	}

}
