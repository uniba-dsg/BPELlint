package bpellint.model.bpel.mex;

import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.bpel.ProcessElement;
import bpellint.model.bpel.ScopeElement;
import bpellint.model.bpel.var.FromPartElement;
import bpellint.model.bpel.var.FromPartsElement;
import bpellint.model.bpel.var.VariableLike;
import bpellint.model.bpel.var.VariableLikeImpl;
import bpellint.model.wsdl.OperationElement;
import bpellint.model.wsdl.PortTypeElement;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;

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

	public ScopeElement getAssociatedScope() {
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

	public MessageExchangeElement getMessageExchange() throws NavigationException {
		return getMessageExchange(getAssociatedScope());
	}

	private MessageExchangeElement getMessageExchange(ScopeElement scope) throws NavigationException {
		Nodes messageExchange = scope.toXOM().query("./bpel:messageExchanges/bpel:messageExchange[@name='" + getMessageExchangeAttribute() + "']",Standards.CONTEXT);
		if (!messageExchange.hasAny()) {
			if (scope instanceof ProcessElement) {
				throw new NavigationException("<messageExchange> of this name ("+getMessageExchangeAttribute()+") is missing.");
			} else {
				return getMessageExchange(scope.getEnclosingScope());
			}
		}
		return new MessageExchangeElement(messageExchange.get(0), getProcessContainer());
	}

	@Override
	public PropertyAliasElement resolvePropertyAlias(PropertyElement property)
			throws NavigationException {
		return variableDelegate.resolvePropertyAlias(property);
	}

}
