package isabel.model.bpel.mex;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;

public class OnEventElement extends NodeHelper implements MessageActivity{

    private MessageActivity delegate;

    public OnEventElement(Node node, ProcessContainer processContainer) {
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

}
