package bpellint.core.model.bpel.mex;

import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;
import bpellint.core.model.bpel.CorrelationElement;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.bpel.var.FromPartElement;
import bpellint.core.model.bpel.var.FromPartsElement;
import bpellint.core.model.wsdl.OperationElement;
import bpellint.core.model.wsdl.PortTypeElement;

import nu.xom.Node;
import nu.xom.Nodes;

public class ReceiveElement extends ContainerAwareReferable implements StartActivity, MessageActivity{

    private final MessageActivity delegate;
	private final NodeHelper receive;

    public ReceiveElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		receive = new NodeHelper(node, "receive");
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

	public boolean hasFromParts() {
		return receive.hasQueryResult("bpel:fromParts");
	}
	
	public List<FromPartElement> getFromParts() throws NavigationException{
		Nodes fromParts = toXOM().query("./bpel:fromParts", Standards.CONTEXT);
		if(!fromParts.hasAny()){
			throw new NavigationException("<receive> has no <fromParts>");
		}
		
		return new FromPartsElement(fromParts.get(0), getProcessContainer()).getAllFromParts();
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
