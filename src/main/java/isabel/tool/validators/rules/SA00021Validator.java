package isabel.tool.validators.rules;

import isabel.model.ElementIdentifier;
import isabel.model.bpel.CorrelationSetElement;
import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.util.List;

import static isabel.tool.impl.Standards.CONTEXT;

public class SA00021Validator extends Validator {

    public SA00021Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        verifyThatEachCorrelationSetHasAnExistingProperty();

        validateFor("//bpel:from[@property]");
        validateFor("//bpel:to[@property]");
    }

    private void validateFor(String toOrFrom) {
        Nodes fromToSet = fileHandler.getBpel().getDocument()
                .query(toOrFrom, CONTEXT);
        for (Node fromTo : fromToSet) {
            String property = new NodeHelper(fromTo).getAttribute("property");
            try {
                Element variable = getCorrespondingVariable(fromTo).asElement();
                hasCorrespondingPropertyAlias(variable, property, fromTo);
            } catch (NavigationException e) {
                addViolation(fromTo);
            }
        }
    }

    private NodeHelper getCorrespondingVariable(Node fromTo) throws NavigationException {
        return new NodeHelper(fromTo).getVariableByName(new NodeHelper(fromTo).getAttribute("variable"));
    }

    private void hasCorrespondingPropertyAlias(Element element, String property,
                                               Node partHolder) throws NavigationException {
        String type = "";

        if (element.getLocalName().equals("onEvent")) {
            type = getOnEventVariableType(element);
        } else if (element.getLocalName().equals("variable")) {
            type = getEnclosingScopeVariableType(element);
        }

        if (!type.isEmpty()) {
            Document wsdl = getCorrespondingWsdl(property, element);
            Nodes wsdlPropertyAliasSet = wsdl.query("//vprop:propertyAlias",
                    CONTEXT);
            for (Node propertyAlias : wsdlPropertyAliasSet) {
                String propertyName = PrefixHelper.removePrefix(new NodeHelper(
                        propertyAlias).getAttribute("propertyName"));

                if (PrefixHelper.removePrefix(property).equals(propertyName)) {
                    if (isOfThisMessageType(type, propertyAlias, partHolder)
                            || isOfThisType(type, propertyAlias) || isOfThisElement(
                            type, propertyAlias)) {
                        return;
                    }
                }
            }
            addViolation(element);
        }
    }

    private boolean isOfThisElement(String type, Node propertyAlias) {
        return new NodeHelper(propertyAlias).getAttribute("element").equals(
                PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisType(String type, Node propertyAlias) {
        return new NodeHelper(propertyAlias).getAttribute("type").equals(
                PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisMessageType(String type, Node propertyAlias,
                                        Node partHolder) {
        NodeHelper nodeHelper = new NodeHelper(propertyAlias);
        String messageType = PrefixHelper.removePrefix(nodeHelper
                .getAttribute("messageType"));
        boolean isMessageType = messageType.equals(PrefixHelper
                .removePrefix(type));

        if (isMessageType) {
            return (new NodeHelper(partHolder).hasSameAttribute(nodeHelper,
                    "part") || hasOneMessagePart(propertyAlias, messageType));
        }

        return false;
    }

    private boolean hasOneMessagePart(Node propertyAlias, String messageType) {
        return propertyAlias
                .getDocument()
                .query("//wsdl:message[@name=\"" + messageType
                        + "\"]/wsdl:part", CONTEXT).size() == 1;
    }

    private Document getCorrespondingWsdl(String property, Node node)
            throws NavigationException {
        String targetNamespace = navigator.getPrefixNamespaceURI(
                node.getDocument(), PrefixHelper.getPrefix(property));
        return fileHandler.getWsdlByTargetNamespace(targetNamespace);
    }

    private void verifyThatEachCorrelationSetHasAnExistingProperty() {
        Nodes correlationSets = fileHandler.getCorrelationSets();
        List<String> properties = ElementIdentifier.toIdentifiers(fileHandler.getProperties());

        for (Node correlationSet : correlationSets) {
            List<String> propertyIdentifiers = new CorrelationSetElement(correlationSet).getPropertyIdentifiers();
            propertyIdentifiers.removeAll(properties);

            if (!propertyIdentifiers.isEmpty()) {
                Logger.warn("Could not find any wsdl:property elements for bpel:correlationSet@properties {0}", propertyIdentifiers);
                addViolation(correlationSet);
            }
        }
    }

    private String getEnclosingScopeVariableType(Node variable)
            throws NavigationException {
        NodeHelper variableHelper = new NodeHelper(variable);
        if (variableHelper.hasAttribute("messageType")) {
            return variableHelper.getAttribute("messageType");
        } else if (variableHelper.hasAttribute("type")) {
            return variableHelper.getAttribute("type");
        } else if (variableHelper.hasAttribute("element")) {
            return variableHelper.getAttribute("element");
        } else {
            throw new NavigationException(
                    "Node variable does not contain any messageType, type or element attribute");
        }
    }

    private String getOnEventVariableType(Node onEvent)
            throws NavigationException {

        NodeHelper onEventHelper = new NodeHelper(onEvent);
        if (onEventHelper.hasAttribute("messageType")) {
            return onEventHelper.getAttribute("messageType");
        } else if (onEventHelper.hasAttribute("element")) {
            return onEventHelper.getAttribute("element");
        } else {
            throw new NavigationException(
                    "Node onEvent does not contain any messageType or element attribute");
        }
    }

    @Override
    public int getSaNumber() {
        return 21;
    }

}
