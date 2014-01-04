package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.fct.CatchElement;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.bpel.mex.OnEventElement;
import isabel.model.bpel.var.VariableElement;
import isabel.model.bpel.var.VariableLike;
import nu.xom.*;

import java.util.List;
import java.util.Objects;

import static isabel.model.Standards.CONTEXT;

public class ValidatorNavigator {

	private ProcessContainer processContainer;

	public ValidatorNavigator(ProcessContainer processContainer) {
		this.processContainer = processContainer;
	}
	
    public Node operationToMessage(List<XmlFile> wsdlImports,
                                   Node operation) throws NavigationException {

        if (operation == null)
            throw new NavigationException("Operation not defined");

        Nodes inputNodes = operation.query("child::wsdl:input/@message",
                CONTEXT);

        if (inputNodes.isEmpty()) {
            return null;
        }

        String messageQName = inputNodes.get(0).getValue();
        String qNamePrefix = PrefixHelper.getPrefix(messageQName);
        String messageName = PrefixHelper.removePrefix(messageQName);
        String messageNamespaceURI = PrefixHelper.getPrefixNamespaceURI(
                operation.getDocument(), qNamePrefix);

        Node message = null;
        for (XmlFile wsdlEntry : wsdlImports) {
            String targetNamespace = wsdlEntry.getTargetNamespace();
            if (targetNamespace.equals(messageNamespaceURI)) {
                Nodes messageNodes = wsdlEntry.getDocument().query(
                        "//wsdl:message[@name='" + messageName + "']", CONTEXT);

                if (messageNodes.hasAny()) {
                    message = messageNodes.get(0);
                    break;
                }

            }
        }

        return message;
    }

    public Node getCorrespondingOutgoingMessage(MessageActivity messageActivity)
            throws NavigationException {

        Node operation = messageActivity.getOperation().toXOM();
        String messageAttribute = getAttributeValue(operation.query("wsdl:output/@message", CONTEXT));
        Document correspondingWsdl = operation.getDocument();
        Nodes messages = correspondingWsdl.query("//wsdl:message", CONTEXT);
        for (Node message : messages) {
            String messageName = new NodeHelper(message).getAttribute("name");
            if (messageName.equals(PrefixHelper.removePrefix(messageAttribute))) {
                return message;
            }
        }
        throw new NavigationException("corresponding <message> is not defined");
    }

    public Node getCorrespondingIncomingMessage(MessageActivity messageActivity)
            throws NavigationException {

        Node operation = messageActivity.getOperation().toXOM();
        String messageAttribute = getAttributeValue(operation.query("wsdl:input/@message", CONTEXT));
        Document correspondingWsdl = operation.getDocument();
        Nodes messages = correspondingWsdl.query("//wsdl:message", CONTEXT);
        for (Node message : messages) {
            String messageName = new NodeHelper(message).getAttribute("name");
            if (messageName.equals(PrefixHelper.removePrefix(messageAttribute))) {
                return message;
            }
        }
        throw new NavigationException("corresponding <message> is not defined");
    }

    public boolean hasInputVariable(MessageActivity msgActivity) {
        Nodes inputVar = msgActivity.toXOM().query("attribute::inputVariable", CONTEXT);
        return inputVar.hasAny();
    }

    public boolean hasOutputVariable(MessageActivity msgActivity) {
        Nodes outVar = msgActivity.toXOM().query("attribute::outputVariable", CONTEXT);
        return outVar.hasAny();
    }

    public static String getAttributeValue(Nodes attributes) {
        if (attributes.hasAny()) {
            Node attribute = attributes.get(0);
            if (attribute instanceof Attribute) {
                return attribute.getValue();
            }
        }
        return "";
    }
    
    public VariableLike getVariableByName(Referable referable, String variableName) throws NavigationException {
        Objects.requireNonNull(variableName, "VariableName must not be null!");

        NodeHelper element = new NodeHelper(referable);
        String elementName = element.getLocalName();

        if ("scope".equals(elementName) || "process".equals(elementName)) {
            Nodes variable = element.toXOM().query("./bpel:variables/bpel:variable[@name='" + variableName + "']", CONTEXT);
            if (variable != null && !variable.isEmpty()) {
                return new VariableElement(variable.get(0), processContainer);
            }
            if ("process".equals(elementName)) {
                throw new NavigationException("Variable does not exist.");
            }
        }

        if ("onEvent".equals(elementName)) {
            if (variableName.equals(element.getAttribute("variable"))) {
                return new OnEventElement(element.toXOM(), processContainer);
            }
        }
        if ("catch".equals(elementName)) {
            if (variableName.equals(element.getAttribute("faultVariable"))) {
                return new CatchElement(element.toXOM(), processContainer);
            }
        }

        return getVariableByName(element.getParent(), variableName);
    }
}
