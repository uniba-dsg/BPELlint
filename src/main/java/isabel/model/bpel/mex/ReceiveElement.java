package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.var.FromPartElement;
import isabel.model.bpel.var.FromPartsContainer;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Node;
import nu.xom.Nodes;

public class ReceiveElement extends ContainerAwareReferable implements StartActivity, MessageActivity{

    private final MessageActivity delegate;
	private final NodeHelper receive;

    public ReceiveElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		receive = new NodeHelper(node, "receive");
        delegate = new MessageActivityImpl(node, processContainer);
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

	public boolean hasFromParts() {
		return receive.hasQueryResult("bpel:fromParts");
	}
	
	public List<FromPartElement> getFromParts() throws NavigationException{
		Nodes fromParts = toXOM().query("./bpel:fromParts", Standards.CONTEXT);
		if(!fromParts.hasAny()){
			throw new NavigationException("<receive> has no <fromParts>");
		}
		
		return new FromPartsContainer(fromParts.get(0), getProcessContainer()).getAllFromParts();
	}

	public String getVariableAttribute() throws NavigationException{
		String variableName = receive.getAttribute("variable");
		if (variableName.isEmpty()) {
			throw new NavigationException("<receive> has no variable attribute");
		} 
		
		return variableName;
	}
	
	public boolean hasVariable() {
		return receive.hasAttribute("variable");
	}

    @Override
    public boolean isStartActivity() {
        return receive.hasAttribute("createInstance") && "yes".equals(receive.getAttribute("createInstance"));
    }



}
