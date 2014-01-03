package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.CorrelationSetElement;
import isabel.model.bpel.var.VariableElement;
import isabel.model.bpel.var.VariableLike;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
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
                VariableLike variable = getCorrespondingVariable(fromTo);
                hasCorrespondingPropertyAlias(variable, property, fromTo);
            } catch (NavigationException e) {
                addViolation(fromTo);
            }
        }
    }

    private VariableLike getCorrespondingVariable(Node fromToNode) throws NavigationException {
        NodeHelper fromTo = new NodeHelper(fromToNode);
		return navigator.getVariableByName(fromTo, fromTo.getAttribute("variable"));
    }

    private void hasCorrespondingPropertyAlias(VariableLike variable, String property,
                                               Node partHolder) throws NavigationException {
        String type = getVariableType(variable);

        if (!type.isEmpty()) {
            XmlFile wsdl = getCorrespondingWsdl(property, variable.toXOM());
            for (PropertyAliasElement propertyAlias : fileHandler.getPropertyAliases(wsdl)) {
                String propertyName = PrefixHelper.removePrefix(propertyAlias.getPropertyNameAttribute());

                if (PrefixHelper.removePrefix(property).equals(propertyName)) {
                    if (isOfThisMessageType(type, propertyAlias, partHolder) || isOfThisType(type, propertyAlias) || isOfThisElement(type, propertyAlias)) {
                        return;
                    }
                }
            }
            addViolation(variable);
        }
    }

    private boolean isOfThisElement(String type, PropertyAliasElement propertyAlias) {
        return propertyAlias.getElementAttribute().equals(PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisType(String type, PropertyAliasElement propertyAlias) {
        return propertyAlias.getTypeAttribute().equals(PrefixHelper.removePrefix(type));
    }

    private boolean isOfThisMessageType(String type, PropertyAliasElement propertyAlias,
                                        Node partHolder) {
        String messageType = PrefixHelper.removePrefix(propertyAlias.getMessageTypeAttribute());
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
        List<String> properties = toIdentifiers(fileHandler.getAllProperties());

        for (Node correlationSet : correlationSets) {
            List<String> propertyIdentifiers = new CorrelationSetElement(correlationSet).getPropertyIdentifiers();
            propertyIdentifiers.removeAll(properties);

            if (!propertyIdentifiers.isEmpty()) {
                Logger.warn("Could not find any wsdl:property elements for bpel:correlationSet@properties {0}", propertyIdentifiers);
                addViolation(correlationSet);
            }
        }
    }
    
    private List<String> toIdentifiers(List<PropertyElement> properties){
        List<String> identifiers = new ArrayList<>();

        for(PropertyElement property : properties){
            identifiers.add(property.toIdentifier());
        }

        return identifiers;
    }

	private String getVariableType(VariableLike variable) throws NavigationException {
		if (variable.hasVariableMessageType()) {
			return variable.getVariableMessageType();
		} else if (variable.hasVariableElement()) {
			return variable.getVariableElement();
		} else if (variable instanceof VariableElement && new VariableElement(variable.toXOM(), fileHandler).hasType()) {
			return new VariableElement(variable.toXOM(), fileHandler).getType();
		} else {
			throw new NavigationException("This variable is untyped.");
		}
	}

    @Override
    public int getSaNumber() {
        return 21;
    }

}
