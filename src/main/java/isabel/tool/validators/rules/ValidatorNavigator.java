package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.mex.MessageActivity;
import isabel.model.wsdl.OperationElement;
import nu.xom.*;

import java.util.HashMap;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class ValidatorNavigator {

    private final ProcessContainer fileHandler;

    public ValidatorNavigator(ProcessContainer fileHandler) {
        this.fileHandler = fileHandler;
    }

    public HashMap<String, Node> getOperationMessages(
            List<XmlFile> wsdlImports, OperationElement operation)
            throws NavigationException {

        if (operation == null)
            return null;

        Nodes operationChildren = operation.asElement().query("child::*", CONTEXT);
        HashMap<String, Node> messages = new HashMap<>();
        for (Node child : operationChildren) {

            NodeHelper childHelper = new NodeHelper(child);
            String messageQName = childHelper.getAttribute("message");
            String childName = childHelper.getLocalName();
            String namespaceURI = operation.asElement().getDocument().getRootElement()
                    .getNamespaceURI(PrefixHelper.getPrefix(messageQName));
            String messageName = PrefixHelper.removePrefix(messageQName);

            Node message = getMessage(messageName, namespaceURI, wsdlImports);
            if (message != null)
                messages.put(childName, message);
        }

        if (messages.isEmpty())
            throw new NavigationException("Messages are not defined.");

        return messages;
    }

    public Node getMessage(String messageName, String namespaceURI,
                           List<XmlFile> wsdlImports) {
        Node message = null;
        for (XmlFile wsdlEntry : wsdlImports) {
            String targetNamespace = wsdlEntry.getTargetNamespace();
            if (targetNamespace.equals(namespaceURI)) {
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

    public XmlFile getCorrespondingWsdlToCorrelationSet(
            Node correlationSet) throws NavigationException {
        String namespacePrefix = getCorrelationPropertyAliasPrefix(correlationSet);
        String correspondingTargetNamespace = getImportNamespace(correlationSet, namespacePrefix);

        for (XmlFile wsdlFile : fileHandler.getWsdls()) {
            if (wsdlFile.getTargetNamespace().equals(correspondingTargetNamespace)) {
                return wsdlFile;
            }
        }
        throw new NavigationException("Referenced WSDL File does not exist.");
    }

    public String getCorrelationPropertyAliasPrefix(Node correlationSet)
            throws NavigationException {

        String propertyAliasName = new NodeHelper(correlationSet).getAttribute("properties");
        String namespacePrefix = PrefixHelper.getPrefix(propertyAliasName);

        if (namespacePrefix != null) {
            return namespacePrefix;
        }

        throw new NavigationException("<correlationSet>@properties prefix does not exist.");
    }


    public String getImportNamespace(Node node, String namespacePrefix) {
        Element rootElement = node.getDocument().getRootElement();

        try {
            return rootElement.getNamespaceURI(namespacePrefix);
        } catch (NullPointerException e) {
            /*
             * if the prefix is undefined in the root element, getNamespaceURI
			 * will throw a nullPointerException.
			 */
            return "";
        }
    }

    public Node getCorrespondingPropertyAlias(Node correlationSet,
                                              Document wsdlFile) throws NavigationException {
        Nodes propertyAliases = wsdlFile
                .query("//vprop:propertyAlias", CONTEXT);
        String propertyAliasAttribute = PrefixHelper
                .removePrefix(new NodeHelper(correlationSet)
                        .getAttribute("properties"));

        return navigateFromPropertyAliasNameToPropertyAlias(propertyAliases,
                propertyAliasAttribute);
    }

    public Node navigateFromPropertyAliasNameToPropertyAlias(Nodes aliases,
                                                             String propertyAliasAttribute) throws NavigationException {
        for (Node propertyAlias : aliases) {
            String propertyAliasName = PrefixHelper
                    .removePrefix(new NodeHelper(propertyAlias)
                            .getAttribute("propertyName"));

            if (propertyAliasName.equals(propertyAliasAttribute)) {
                return propertyAlias;
            }
        }
        throw new NavigationException(
                "Referenced <propertyAlias> does not exist.");
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

}
