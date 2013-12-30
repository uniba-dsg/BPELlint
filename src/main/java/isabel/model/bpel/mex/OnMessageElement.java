package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.PickElement;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;

public class OnMessageElement extends NodeHelper implements MessageActivity, StartActivity {

    private MessageActivity delegate;

    public OnMessageElement(Node node, ProcessContainer processContainer) {
        super(node);

        delegate = new MessageActivityImpl(this, processContainer);
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
	public List<CorrelationElement> getCorrelations() throws NavigationException {
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
    public Node toXOM() {
        return delegate.toXOM();
    }

    public boolean hasVariable() {
        return hasAttribute("variable");
    }

    public boolean hasFromParts() {
        return hasQueryResult("bpel:fromParts");
    }

	@Override
	public boolean isStartActivity() {
		return getPick().isStartActivity();
	}

	private PickElement getPick() {
		return new PickElement(getParent());
	}
}
