package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;
import static isabel.tool.impl.Standards.XSD_NAMESPACE;

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
		XmlFile wsdlEntry = navigator
				.getCorrespondingWsdlToCorrelationSet(correlationSet);
		Document wsdlFile = wsdlEntry.getDocument();
		Node propertyAlias = navigator.getCorrespondingPropertyAlias(
				correlationSet, wsdlFile);
		Node property = navigator.getCorrespondingProperty(propertyAlias);

		String propertyType = new NodeHelper(property).getAttribute("type");
		String namespacePrefix = PrefixHelper.getPrefix(propertyType);
		String propertyTypeTargetNamespace = navigator.getImportNamespace(
				property, namespacePrefix);

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
