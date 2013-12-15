package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;

public class ReceiveElement extends NodeHelper implements StartActivity, MessageActivity{

    private final MessageActivity delegate;

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
    public Node toXOM() {
        return delegate.toXOM();
    }

    public ReceiveElement(Node receive, ProcessContainer processContainer) {
		super(receive);

		if (!getLocalName().equals("receive")) {
			throw new IllegalArgumentException(
					"receive helper only works for bpel:receive elements");
		}

        delegate = new MessageActivityImpl(this, processContainer);
	}

	public boolean hasFromParts() {
		return hasQueryResult("bpel:fromParts");
	}

	public boolean hasVariable() {
		return hasAttribute("variable");
	}

    @Override
    public boolean isStartActivity() {
        return hasAttribute("createInstance") && "yes".equals(getAttribute("createInstance"));
    }



}
