package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.XSD_NAMESPACE;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.prefixFree;

public class SA00045Validator extends Validator {
	private String filePath;

	public SA00045Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
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

		String propertyType = getAttributeValue(property
				.query("@type", CONTEXT));
		String namespacePrefix = navigator.getPrefix(propertyType);
		String propertyTypeTargetNamespace = navigator.getImportNamespace(
				property, namespacePrefix);

		if (XSD_NAMESPACE.equals(propertyTypeTargetNamespace)) {
			Document xmlSchema = navigator.getXmlSchema();
			Nodes simpleTypes = xmlSchema.query("//xsd:simpleType", CONTEXT);
			for (Node simpleType : simpleTypes) {
				String simpleTypeName = getAttributeValue(simpleType.query(
						"@name", CONTEXT));

				if (prefixFree(propertyType).equals(simpleTypeName)) {
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
