package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.PrefixHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.XSD_NAMESPACE;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00045Validator extends Validator {
	private String filePath;

	public SA00045Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes allCorrelationSet = fileHandler.getBpel().getDocument()
				.query("//bpel:correlationSet", CONTEXT);

		for (Node correlationSet : allCorrelationSet) {
			try {
				if (!isSimpleType(correlationSet)) {
					addViolation(filePath, correlationSet, 1);
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
	}

	private boolean isSimpleType(Node correlationSet)
			throws NavigationException {
		DocumentEntry wsdlEntry = navigator
				.getCorrespondingWsdlToCorrelationSet(correlationSet);
		filePath = wsdlEntry.getFilePath();
		Document wsdlFile = wsdlEntry.getDocument();
		Node propertyAlias = navigator.getCorrespondingPropertyAlias(
				correlationSet, wsdlFile);
		Node property = navigator.getCorrespondingProperty(propertyAlias);

		String propertyType = new NodeHelper(property).getAttributeByName("type");
		String namespacePrefix = PrefixHelper.getPrefix(propertyType);
		String propertyTypeTargetNamespace = navigator.getImportNamespace(
				property, namespacePrefix);

		if (XSD_NAMESPACE.equals(propertyTypeTargetNamespace)) {
			Document xmlSchema = fileHandler.getXmlSchema();
			Nodes simpleTypes = xmlSchema.query("//xsd:simpleType", CONTEXT);
			for (Node simpleType : simpleTypes) {
				String simpleTypeName = new NodeHelper(simpleType).getAttributeByName("name");

				if (PrefixHelper.removePrefix(propertyType).equals(simpleTypeName)) {
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
