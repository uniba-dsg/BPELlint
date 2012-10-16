package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00002Validator extends Validator {

	public SA00002Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			String filePath = wsdlEntry.getFilePath();

			Nodes portTypes = getPortTypes(wsdlEntry);
			for (int i = 0; i < portTypes.size(); i++) {
				Nodes operationNames = getOperationNames(portTypes.get(i));

				Set<String> nameSet = new HashSet<>();
				for (int j = 0; j < operationNames.size(); j++) {
					Node nameAttribute = operationNames.get(j);
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
