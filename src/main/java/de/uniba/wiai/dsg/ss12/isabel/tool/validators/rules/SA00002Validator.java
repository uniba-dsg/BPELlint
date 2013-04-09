package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;

public class SA00002Validator extends Validator {

	public SA00002Validator(BpelProcessFiles files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			for (Node portType : getPortTypes(wsdlEntry)) {
				Set<String> nameSet = new HashSet<>();
				for (Node nameAttribute : getOperationNames(portType)) {
					String currentName = nameAttribute.toXML();

					if (nameSet.contains(currentName)) {
						addViolation(nameAttribute);
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
		return wsdlEntry.getDocument().query("//wsdl:portType", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 2;
	}
}
