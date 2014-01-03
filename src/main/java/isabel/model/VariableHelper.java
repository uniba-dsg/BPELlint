package isabel.model;

import static isabel.model.Standards.CONTEXT;
import isabel.model.bpel.var.VariableElement;
import isabel.model.bpel.var.VariableLike;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
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
		} else if (variable instanceof VariableElement) {
			VariableElement variableElement = (VariableElement) variable;
			if (!variableElement.getType().isEmpty()) {
				if (message.hasOnlyOnePart()) {
					Node messagePartXsdType = findMessageXsdElement(message);
					Node variableXsdType = findXsdType(variableElement.getType(), variableElement.toXOM());
					if (hasSameNameAttribute(variableXsdType, messagePartXsdType)) {
						return true;
					}
				}
			}
		}
		return false;
	}

    private Node findMessageXsdElement(MessageElement message) {
        PartElement partElement = message.getSinglePart();
        Node xsdElement = findXsdType(partElement.getElement(), partElement.toXOM());
        String elementTypeQName = new NodeHelper(xsdElement)
                .getAttribute("type");
        return findXsdType(elementTypeQName, partElement.toXOM());
    }

    private boolean hasSameNameAttribute(Node xsdType, Node xsdSecType) {
        return new NodeHelper(xsdType).getAttribute("name").equals(
                new NodeHelper(xsdSecType).getAttribute("name"));
    }

    private Node findXsdType(String typeQName, Node variable) {
        String variableTypeNamespaceURI = variable.getDocument()
                .getRootElement()
                .getNamespaceURI(PrefixHelper.getPrefix(typeQName));
        String xsdTypeName = PrefixHelper.removePrefix(typeQName);
        Node xsdType = null;

        for (Node node : processContainer.getSchemas()) {
            if (new NodeHelper(node)
                    .hasTargetNamespace(variableTypeNamespaceURI)) {
                Nodes xsdTypes = node.getDocument().query("//*[@name='" + xsdTypeName + "']", CONTEXT);
                if (xsdTypes.hasAny()) {
                    xsdType = xsdTypes.get(0);
                    break;
                }
            }
        }
        return xsdType;
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
