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
import static isabel.model.Standards.CONTEXT;

public class InvokeElement extends NodeHelper implements MessageActivity {

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

    public InvokeElement(Node node, ProcessContainer processContainer) {
        super(node);

        delegate = new MessageActivityImpl(this, processContainer);
    }

    public String getInputVariableAttribute() {
        return getAttribute("inputVariable");
    }

    public String getOutputVariableAttribute() {
        return getAttribute("outputVariable");
    }

    public boolean hasInputVariable() {
        return toXOM().query("@inputVariable", CONTEXT).hasAny();
    }

    public boolean hasToParts() {
        return toXOM().query("bpel:toParts", CONTEXT).hasAny();
    }

    public boolean hasFromParts() {
        return toXOM().query("bpel:fromParts", CONTEXT).hasAny();
    }

    public boolean hasOutputVariable() {
        return !getOutputVariableAttribute().isEmpty();
    }

}
