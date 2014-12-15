package bpellint.core.validators.rules;

import static bpellint.core.model.Standards.CONTEXT;

import api.SimpleValidationResult;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import bpellint.core.model.NavigationException;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.SimpleTypeChecker;
import bpellint.core.model.XmlFile;
import bpellint.core.model.bpel.CorrelationSetElement;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;

public class SA00045Validator extends Validator {

    public SA00045Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (CorrelationSetElement correlationSet : processContainer.getAllCorrelationSets()) {
            try {
                if (!isSimpleType(correlationSet)) {
                    addViolation(correlationSet);
                }
            } catch (NavigationException e) {
                // This node could not be validated
            }
        }
    }

    private boolean isSimpleType(CorrelationSetElement correlationSet)
            throws NavigationException {
        PropertyElement property = getPropertyAlias(correlationSet).getProperty();

        String propertyType = property.getTypeAttribute();
        return new SimpleTypeChecker(processContainer, property).isSimpleType(propertyType);
    }

	private PropertyAliasElement getPropertyAlias(CorrelationSetElement correlationSet) throws NavigationException {
        XmlFile wsdlEntry = getCorrespondingWsdlToCorrelationSet(correlationSet);
        Document wsdlFile = wsdlEntry.getDocument();
        return getCorrespondingPropertyAlias(correlationSet, wsdlFile);
    }

    public PropertyAliasElement getCorrespondingPropertyAlias(CorrelationSetElement correlationSet, Document wsdlFile) throws NavigationException {
        Nodes propertyAliases = wsdlFile.query("//vprop:propertyAlias", CONTEXT);
        String propertyAliasAttribute = PrefixHelper.removePrefix(correlationSet.getPropertiesAttribute());

        return navigateFromPropertyAliasNameToPropertyAlias(propertyAliases,
                propertyAliasAttribute);
    }

    public PropertyAliasElement navigateFromPropertyAliasNameToPropertyAlias(Nodes aliases, String propertyAliasAttribute) throws NavigationException {
        for (Node propertyAlias : aliases) {
            PropertyAliasElement propertyAliasElement = new PropertyAliasElement(propertyAlias, processContainer);
            String propertyAliasName = PrefixHelper.removePrefix(propertyAliasElement.getPropertyName());

            if (propertyAliasName.equals(propertyAliasAttribute)) {
                return propertyAliasElement;
            }
        }
        throw new NavigationException(
                "Referenced <propertyAlias> does not exist.");
    }

    public XmlFile getCorrespondingWsdlToCorrelationSet(CorrelationSetElement correlationSet) throws NavigationException {
        String namespacePrefix = correlationSet.getCorrelationPropertyAliasPrefix();
        String correspondingTargetNamespace = SimpleTypeChecker.getImportNamespace(correlationSet, namespacePrefix);

        for (XmlFile wsdlFile : processContainer.getWsdls()) {
            if (wsdlFile.getTargetNamespace().equals(correspondingTargetNamespace)) {
                return wsdlFile;
            }
        }
        throw new NavigationException("Referenced WSDL File does not exist.");
    }

    @Override
    public int getSaNumber() {
        return 45;
    }
}
