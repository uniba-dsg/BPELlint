package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.mex.MessageActivity;
import nu.xom.*;

import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class ValidatorNavigator {

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
