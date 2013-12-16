package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
import isabel.tool.validators.rules.ValidatorNavigator;
import nu.xom.Node;
import nu.xom.Nodes;
import static isabel.model.Standards.CONTEXT;

public class VariableElement extends NodeHelper {

    private ProcessContainer processContainer;

	public VariableElement(Node node) {
        super(node);

        // node can be either an OnEvent as well as a variable
    }

    public boolean hasMessageType() {
        return node.query("@messageType", CONTEXT).hasAny();
    }

    public boolean hasType() {
        return node.query("@type", CONTEXT).hasAny();
    }

    public boolean hasElement() {
        return node.query("@element", CONTEXT).hasAny();
    }

    public String getName() {
        return getAttribute("name");
    }

    public String getType() {
        return getAttribute("type");
    }
    
    public String getMessageType() {
        return getAttribute("messageType");
    }
    
    public boolean hasCorrespondingMessage(MessageElement message, ProcessContainer processContainer) {
        this.processContainer = processContainer;
		if (!getMessageType().isEmpty()) {
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

    private MessageElement getVariableMessage() {
        String namespaceURI = toXOM().getDocument().getRootElement()
                .getNamespaceURI(PrefixHelper.getPrefix(getMessageType()));
        String messageName = PrefixHelper.removePrefix(getMessageType());

        return new MessageElement(new ValidatorNavigator().getMessage(messageName, namespaceURI, processContainer.getWsdls()));
    }

}
