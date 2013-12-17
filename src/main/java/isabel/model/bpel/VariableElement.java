package isabel.model.bpel;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
import nu.xom.Node;
import nu.xom.Nodes;
import static isabel.model.Standards.CONTEXT;

public class VariableElement extends NodeHelper implements VariableLike {

    private ProcessContainer processContainer;

	public VariableElement(Node node) {
        super(node);

        // node can be either an OnEvent as well as a variable
    }

	@Override
    public boolean hasVariableMessageType() {
        return node.query("@messageType", CONTEXT).hasAny();
    }

    public boolean hasType() {
        return node.query("@type", CONTEXT).hasAny();
    }

    @Override
    public boolean hasVariableElement() {
        return node.query("@element", CONTEXT).hasAny();
    }

	@Override
	public String getVariableElement() {
		return getAttribute("element");
	}
	
	@Override
    public String getVariableName() {
        return getAttribute("name");
    }

    public String getType() {
        return getAttribute("type");
    }
    
    @Override
    public String getVariableMessageType() {
        return getAttribute("messageType");
    }
    
    public boolean hasCorrespondingMessage(MessageElement message, ProcessContainer processContainer) throws NavigationException {
        this.processContainer = processContainer;
		if (!getVariableMessageType().isEmpty()) {
            MessageElement variableMessage = getVariableMessage();
            if (message.equals(variableMessage)) {
                return true;
            }
        } else if (!getType().isEmpty()) {
            if (message.hasOnlyOnePart()) {
                Node messagePartXsdType = findMessageXsdElement(message);
                Node variableXsdType = findXsdType(getType(), toXOM());
                if (hasSameNameAttribute(variableXsdType, messagePartXsdType)) {
                    return true;
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
        String namespaceURI = toXOM().getDocument().getRootElement()
                .getNamespaceURI(PrefixHelper.getPrefix(getVariableMessageType()));
        String messageName = PrefixHelper.removePrefix(getVariableMessageType());
		for (XmlFile wsdlEntry : processContainer.getWsdls()) {
		    String targetNamespace = wsdlEntry.getTargetNamespace();
		    if (targetNamespace.equals(namespaceURI)) {
		        Nodes messageNodes = wsdlEntry.getDocument().query(
		                "//wsdl:message[@name='" + messageName + "']", CONTEXT);
		
		        if (messageNodes.hasAny()) {
		            return new MessageElement(messageNodes.get(0));
		        }
		    }
		}
		
		throw new NavigationException("<message> was not found");
    }

}
