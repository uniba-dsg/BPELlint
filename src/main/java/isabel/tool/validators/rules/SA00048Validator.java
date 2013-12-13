package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.VariableElement;
import isabel.model.bpel.mex.InvokeElement;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;
import java.util.Map;

import static isabel.model.Standards.CONTEXT;

public class SA00048Validator extends Validator {
    public SA00048Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (InvokeElement invoke : fileHandler.getAllInvokes()) {
            try {
                Map<String, Node> messages = getCorrespondingMessages(invoke);
                VariableElement variableForInput = getInputVariable(invoke);
                VariableElement variableForOutput = getOutputVariable(invoke);

                if (variableForInput != null) {
                    if (!hasCorrespondingMessage(variableForInput, messages.get("input")))
                        addViolation(invoke, 1);
                }

                if (variableForOutput != null) {
                    if (!hasCorrespondingMessage(variableForOutput, messages.get("output"))) {
                        addViolation(invoke, 2);
                    }
                }
            } catch (NavigationException e) {
                // This node could not be validated
            }
        }
    }

    private VariableElement getInputVariable(InvokeElement invokeActivity) {
        return correspondingVariable(invokeActivity, invokeActivity.getInputVariableAttribute());
    }

    private VariableElement getOutputVariable(InvokeElement invokeActivity) {
        return correspondingVariable(invokeActivity, invokeActivity.getOutputVariableAttribute());
    }

    private Map<String, Node> getCorrespondingMessages(InvokeElement invokeActivity)
            throws NavigationException {
        List<XmlFile> wsdlImports = fileHandler.getWsdls();
        OperationElement operation = invokeActivity.getOperation();
        return navigator.getOperationMessages(wsdlImports, operation);
    }

    private boolean hasCorrespondingMessage(VariableElement variable, Node operationMessage) {
        String messageTypeQName = variable.getMessageType();
        String typeQName = variable.getType();

        if (!messageTypeQName.isEmpty()) {
            Node variableMessage = getVariableMessage(messageTypeQName, variable);
            if (equalsMessage(variableMessage, operationMessage)) {
                return true;
            }
        } else if (typeQName != null) {
            if (messageHasOnlyOnePartWithElement(operationMessage)) {
                Node messagePartXsdType = findMessageXsdElement(operationMessage);
                Node variableXsdType = findXsdType(typeQName, variable.toXOM());
                if (hasSameNameAttribute(variableXsdType, messagePartXsdType)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Node findMessageXsdElement(Node operationMessage) {
        Node messagePartElement = getMessagePartAttributeElement(operationMessage);
        Node xsdElement = findXsdType(messagePartElement.getValue(),
                messagePartElement);
        String elementTypeQName = new NodeHelper(xsdElement)
                .getAttribute("type");
        return findXsdType(elementTypeQName, messagePartElement);
    }

    private boolean messageHasOnlyOnePartWithElement(Node operationMessage) {
        return operationMessage.query("child::*").size() == 1
                && operationMessage.query(
                "child::wsdl:part[position()=1]/@element", CONTEXT)
                .size() == 1;
    }

    private Node getMessagePartAttributeElement(Node operationMessage) {
        Nodes partElement = operationMessage.query(
                "child::wsdl:part[position()=1]/@element", CONTEXT);
        return (partElement.hasAny()) ? partElement.get(0) : null;
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

        for (Node node : fileHandler.getSchemas()) {
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

    private boolean equalsMessage(Node firstNode, Node secondNode) {
        return equalsTargetNamespace(firstNode, secondNode)
                && hasSameNameAttribute(firstNode, secondNode);
    }

    private boolean equalsTargetNamespace(Node firstNode, Node secondNode) {
        return new NodeHelper(firstNode).hasTargetNamespace(secondNode);
    }

    private Node getVariableMessage(String messageTypeQName, VariableElement variable) {
        String namespaceURI = variable.toXOM().getDocument().getRootElement()
                .getNamespaceURI(PrefixHelper.getPrefix(messageTypeQName));
        String messageName = PrefixHelper.removePrefix(messageTypeQName);

        return navigator.getMessage(messageName, namespaceURI,
                fileHandler.getWsdls());
    }

    private VariableElement correspondingVariable(InvokeElement invokeActivity, String variableName) {
        try {
            return invokeActivity.getVariableByName(variableName);
        } catch (NavigationException e) {
            return null;
        }
    }

    @Override
    public int getSaNumber() {
        return 48;
    }

}
