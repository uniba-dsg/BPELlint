package isabel.model.bpel.mex;

import java.util.List;

import isabel.model.*;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.CorrelationsElement;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.PartnerLinkedImpl;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import nu.xom.Nodes;
import static isabel.model.Standards.CONTEXT;

public class MessageActivityImpl extends ContainerAwareReferable implements MessageActivity {

    private final NodeHelper nodeHelper;
	private final PartnerLinkedImpl partnerLinkedDelegate;

    public MessageActivityImpl(Referable messageActivity, ProcessContainer processContainer) {
    	super(messageActivity.toXOM(), processContainer);
        this.nodeHelper = new NodeHelper(messageActivity);
        partnerLinkedDelegate = new PartnerLinkedImpl(messageActivity, getProcessContainer(), getPartnerLinkAttribute());
    }

    @Override
    public Type getType() {
        if ("receive".equals(nodeHelper.getLocalName())) {
            return Type.RECEIVE;
        } else if ("reply".equals(nodeHelper.getLocalName())) {
            return Type.REPLY;
        } else if ("invoke".equals(nodeHelper.getLocalName())) {
            return Type.INVOKE;
        } else if ("onMessage".equals(nodeHelper.getLocalName())) {
            return Type.ON_MESSAGE;
        } else if ("onEvent".equals(nodeHelper.getLocalName())) {
            return Type.ON_EVENT;
        }

        throw new IllegalStateException("Given node " + nodeHelper.getLocalName() + " is not a message activity");
    }

    @Override
    public PartnerLinkElement getPartnerLink() throws NavigationException {
        return partnerLinkedDelegate.getPartnerLink();
    }

    @Override
    public PortTypeElement getPortType() throws NavigationException {
        return getPartnerLink().partnerLinkToPortType(getProcessContainer(), this);
    }

    @Override
    public OperationElement getOperation() throws NavigationException {
        return getPortType().getOperationByName(getOperationAttribute());
    }

	@Override
	public List<CorrelationElement> getCorrelations() throws NavigationException {
		Nodes correlationsNode = nodeHelper.toXOM().query("./bpel:correlations", CONTEXT);
		if(!correlationsNode.hasAny()){
			throw new NavigationException("Has no correlation Element.");
		}
		return new CorrelationsElement(correlationsNode.get(0), getProcessContainer()).getCorrelationWithoutPattern();
	}
	
    @Override
    public String getPartnerLinkAttribute() {
        return nodeHelper.getAttribute("partnerLink");
    }

    @Override
    public String getOperationAttribute() {
        return nodeHelper.getAttribute("operation");
    }

    @Override
    public String getPortTypeAttribute() {
        return nodeHelper.getAttribute("portType");
    }

    @Override
    public boolean isReceiving() {
        return Type.ON_EVENT.equals(getType()) || Type.RECEIVE.equals(getType()) || Type.ON_MESSAGE.equals(getType());
    }

	@Override
	public String getMessageExchangeAttribute() {
		return nodeHelper.getAttribute("messageExchange");
	}

    public PortTypeElement getPortType(String portTypeQName, String portTypeNamespaceURI)
            throws NavigationException {
        String portTypeName = PrefixHelper.removePrefix(portTypeQName);
        for (XmlFile wsdlEntry : getProcessContainer().getWsdls()) {
            String targetNamespace = wsdlEntry.getTargetNamespace();
            if (targetNamespace.equals(portTypeNamespaceURI)) {
                Nodes portTypes = wsdlEntry.getDocument().query(
                        "//wsdl:portType[@name='" + portTypeName + "']",
                        CONTEXT);
                if (portTypes.hasAny()) {
                    return new PortTypeElement(portTypes.get(0), getProcessContainer());
                }
            }
        }

        throw new NavigationException("portType not defined");
    }

}
