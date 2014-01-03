package isabel.tool.validators.rules;

import isabel.model.bpel.CorrelationSetElement;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.NavigationException;
import isabel.model.Referable;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import static isabel.model.Standards.CONTEXT;
import static isabel.model.Standards.XSD_NAMESPACE;

public class SA00045Validator extends Validator {

    public SA00045Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (CorrelationSetElement correlationSet : fileHandler.getAllCorrelationSets()) {
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
        String namespacePrefix = PrefixHelper.getPrefix(propertyType);
        String propertyTypeTargetNamespace = getImportNamespace(property, namespacePrefix);

        if (XSD_NAMESPACE.equals(propertyTypeTargetNamespace)) {
            Document xmlSchema = fileHandler.getXmlSchema();
            Nodes simpleTypes = xmlSchema.query("//xsd:simpleType", CONTEXT);
            for (Node simpleType : simpleTypes) {
                String simpleTypeName = new NodeHelper(simpleType).getAttribute("name");

                if (PrefixHelper.removePrefix(propertyType).equals(
                        simpleTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getImportNamespace(Referable node, String namespacePrefix) {
        Element rootElement = node.toXOM().getDocument().getRootElement();

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
            PropertyAliasElement propertyAliasElement = new PropertyAliasElement(propertyAlias, fileHandler);
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
        String correspondingTargetNamespace = getImportNamespace(correlationSet, namespacePrefix);

        for (XmlFile wsdlFile : fileHandler.getWsdls()) {
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
