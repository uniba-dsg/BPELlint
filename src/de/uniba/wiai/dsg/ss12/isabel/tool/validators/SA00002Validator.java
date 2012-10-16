package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.HashSet;
import java.util.Set;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00002Validator extends Validator {

	public SA00002Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			String filePath = wsdlEntry.getFilePath();

			for (Node portType : getPortTypes(wsdlEntry)) {
				Set<String> nameSet = new HashSet<>();
				for (Node nameAttribute : getOperationNames(portType)) {
					String currentName = nameAttribute.toXML();

					if (nameSet.contains(currentName)) {
						addViolation(filePath, nameAttribute, 1);
					} else {
						nameSet.add(currentName);
					}
				}
			}
		}
	}

	private Nodes getOperationNames(Node portType) {
		return portType.query("child::wsdl:operation/@name", CONTEXT);
	}

	private Nodes getPortTypes(DocumentEntry wsdlEntry) {
		return wsdlEntry.getDocument().query("//wsdl:portType",
				CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 2;
	}
}
