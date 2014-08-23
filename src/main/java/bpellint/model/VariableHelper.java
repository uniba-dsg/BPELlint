package bpellint.model;

import static bpellint.model.Standards.CONTEXT;

import bpellint.model.bpel.var.VariableLike;
import bpellint.model.wsdl.MessageElement;

import nu.xom.Node;
import nu.xom.Nodes;

public class VariableHelper {
	
	private final ProcessContainer processContainer;
	private final VariableLike variable;
	
	public VariableHelper(ProcessContainer processContainer,
			VariableLike variable) {
		super();
		this.processContainer = processContainer;
		this.variable = variable;
	}

	public boolean hasCorrespondingMessage(MessageElement message)
			throws NavigationException {
		if (!variable.getVariableMessageType().isEmpty()) {
			MessageElement variableMessage = getVariableMessage();
			if (message.equals(variableMessage)) {
				return true;
			}
		} else {
			if (!variable.getVariableElement().isEmpty()) {
				if (message.hasOnlyOnePart()) {
                    Node messagePartXsdType = findElementDefinition(message.getSinglePart().getElement(), message);
					Node variableXsdType = findElementDefinition(variable.getVariableElement(), variable);
                    return areEqual(variableXsdType, messagePartXsdType);
				}
			}
		}
		return false;
	}

    private Node findElementDefinition(String element, Referable referable) throws NavigationException {
        for (XmlFile wsdl : processContainer.getWsdlsAndXsds()) {
            if (wsdl.getTargetNamespace().equals(PrefixHelper.getPrefixNamespaceURI(referable.toXOM(), PrefixHelper.getPrefix(element)))){
                Nodes elementDefinition = wsdl.getDocument().query("//*[@name='" + PrefixHelper.removePrefix(element) + "']", Standards.CONTEXT);
                if (elementDefinition.hasAny()){
                    return elementDefinition.get(0);
                }
            }
        }
        throw new NavigationException("element definition of @element=\""+element +"\" is not found in any WSDL or XSD");
    }

    private boolean areEqual(Node xsdType, Node xsdSecType) {
        return new ComparableNode(xsdType).equals(new ComparableNode(xsdSecType));
    }

    private MessageElement getVariableMessage() throws NavigationException {
        String namespaceURI = variable.toXOM().getDocument().getRootElement()
                .getNamespaceURI(PrefixHelper.getPrefix(variable.getVariableMessageType()));
        String messageName = PrefixHelper.removePrefix(variable.getVariableMessageType());
		for (XmlFile wsdlEntry : processContainer.getWsdls()) {
		    String targetNamespace = wsdlEntry.getTargetNamespace();
		    if (targetNamespace.equals(namespaceURI)) {
		        Nodes messageNodes = wsdlEntry.getDocument().query(
		                "//wsdl:message[@name='" + messageName + "']", CONTEXT);
		
		        if (messageNodes.hasAny()) {
		            return new MessageElement(messageNodes.get(0), processContainer);
		        }
		    }
		}
		
		throw new NavigationException("<message> was not found");
    }
    
}
