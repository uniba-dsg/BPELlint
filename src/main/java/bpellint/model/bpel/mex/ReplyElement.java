package bpellint.model.bpel.mex;

import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.wsdl.OperationElement;
import bpellint.model.wsdl.PortTypeElement;

import nu.xom.Node;

public class ReplyElement extends ContainerAwareReferable implements MessageActivity{

    private final MessageActivity delegate;
	private final NodeHelper reply;

    public ReplyElement(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        reply = new NodeHelper(node, "reply");
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

	public String getVariableAttribute() throws NavigationException {
		String variableName = reply.getAttribute("variable");
		if (variableName.isEmpty()) {
			throw new NavigationException("<reply> has no variable attribute");
		}
		return variableName;
	}

	public boolean hasFaultNameAttribute() {
		return toXOM().query("./@faultName").hasAny();
	}

}
