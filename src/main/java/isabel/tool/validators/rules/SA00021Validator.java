package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.CorrelationSetElement;
import isabel.model.bpel.VariableElement;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.util.List;

import static isabel.model.Standards.CONTEXT;

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
            type = getOnEventVariableType(new NodeHelper(element));
        } else if (element.getLocalName().equals("variable")) {
            type = getEnclosingScopeVariableType(new VariableElement(element));
        }

        if (!type.isEmpty()) {
            XmlFile wsdl = getCorrespondingWsdl(property, element);
            for (PropertyAliasElement propertyAlias : wsdl.getPropertyAliases()) {
                String propertyName = PrefixHelper.removePrefix(propertyAlias.getAttribute("propertyName"));

                if (PrefixHelper.removePrefix(property).equals(propertyName)) {
                    if (isOfThisMessageType(type, propertyAlias, partHolder) || isOfThisType(type, propertyAlias) || isOfThisElement(type, propertyAlias)) {
                        return;
                    }
                }
            }
            addViolation(element);
        }
    }

    private boolean isOfThisElement(String type, PropertyAliasElement propertyAlias) {
        return propertyAlias.getAttribute("element").equals(PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisType(String type, PropertyAliasElement propertyAlias) {
        return propertyAlias.getAttribute("type").equals(PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisMessageType(String type, PropertyAliasElement propertyAlias,
                                        Node partHolder) {
        String messageType = PrefixHelper.removePrefix(propertyAlias
                .getAttribute("messageType"));
        boolean isMessageType = messageType.equals(PrefixHelper
                .removePrefix(type));

        if (isMessageType) {
            return (new NodeHelper(partHolder).hasSameAttribute(propertyAlias, "part") || hasOneMessagePart(propertyAlias, messageType));
        }

        return false;
    }

    private boolean hasOneMessagePart(PropertyAliasElement propertyAlias, String messageType) {
        return propertyAlias.toXOM().getDocument().query("//wsdl:message[@name=\"" + messageType + "\"]/wsdl:part", CONTEXT).size() == 1;
    }

    private XmlFile getCorrespondingWsdl(String property, Node node)
            throws NavigationException {
        String targetNamespace = PrefixHelper.getPrefixNamespaceURI(
                node.getDocument(), PrefixHelper.getPrefix(property));
        return fileHandler.getWsdlByTargetNamespace(targetNamespace);
    }

    private void verifyThatEachCorrelationSetHasAnExistingProperty() {
        Nodes correlationSets = fileHandler.getCorrelationSets();
        List<String> properties = ElementIdentifier.toIdentifiers(fileHandler.getAllProperties());

        for (Node correlationSet : correlationSets) {
            List<String> propertyIdentifiers = new CorrelationSetElement(correlationSet).getPropertyIdentifiers();
            propertyIdentifiers.removeAll(properties);

            if (!propertyIdentifiers.isEmpty()) {
                Logger.warn("Could not find any wsdl:property elements for bpel:correlationSet@properties {0}", propertyIdentifiers);
                addViolation(correlationSet);
            }
        }
    }

    private String getEnclosingScopeVariableType(VariableElement variable)
            throws NavigationException {
        if (variable.hasAttribute("messageType")) {
            return variable.getAttribute("messageType");
        } else if (variable.hasAttribute("type")) {
            return variable.getAttribute("type");
        } else if (variable.hasAttribute("element")) {
            return variable.getAttribute("element");
        } else {
            throw new NavigationException("Node variable does not contain any messageType, type or element attribute");
        }
    }

    private String getOnEventVariableType(NodeHelper onEvent)
            throws NavigationException {

        if (onEvent.hasAttribute("messageType")) {
            return onEvent.getAttribute("messageType");
        } else if (onEvent.hasAttribute("element")) {
            return onEvent.getAttribute("element");
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
