package bpellint.tool.validators.rules;

import static bpellint.model.Standards.CONTEXT;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import bpellint.model.NavigationException;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.SimpleTypeChecker;
import bpellint.model.XmlFile;
import bpellint.model.bpel.CorrelationSetElement;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00045Validator extends Validator {

    public SA00045Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
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
