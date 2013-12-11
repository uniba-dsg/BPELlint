package isabel.tool.validators.rules;

import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Document;
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
		for (Node correlationSet : getAllCorrelationSets()) {
			try {
				if (!isSimpleType(correlationSet)) {
					addViolation(correlationSet);
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
	}

	private Nodes getAllCorrelationSets() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:correlationSet", CONTEXT);
	}

	private boolean isSimpleType(Node correlationSet)
			throws NavigationException {
		XmlFile wsdlEntry = navigator.getCorrespondingWsdlToCorrelationSet(correlationSet);
		Document wsdlFile = wsdlEntry.getDocument();
		Node propertyAlias = navigator.getCorrespondingPropertyAlias(
				correlationSet, wsdlFile);
		PropertyElement property = new PropertyAliasElement(propertyAlias).getCorrespondingProperty();

		String propertyType = property.getAttribute("type");
		String namespacePrefix = PrefixHelper.getPrefix(propertyType);
		String propertyTypeTargetNamespace = navigator.getImportNamespace(
				property.asElement(), namespacePrefix);

		if (XSD_NAMESPACE.equals(propertyTypeTargetNamespace)) {
			Document xmlSchema = fileHandler.getXmlSchema();
			Nodes simpleTypes = xmlSchema.query("//xsd:simpleType", CONTEXT);
			for (Node simpleType : simpleTypes) {
				String simpleTypeName = new NodeHelper(simpleType)
						.getAttribute("name");

				if (PrefixHelper.removePrefix(propertyType).equals(
						simpleTypeName)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getSaNumber() {
		return 45;
	}
}
