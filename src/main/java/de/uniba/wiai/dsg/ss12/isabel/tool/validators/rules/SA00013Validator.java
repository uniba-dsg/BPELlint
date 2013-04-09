package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.bpel.ImportElement;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;

public class SA00013Validator extends Validator {

	public SA00013Validator(BpelProcessFiles files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node importNode : getAllImports()) {
			if (!hasCorrectType(importNode)) {
				addViolation(importNode);
			}
		}
	}

	private Nodes getAllImports() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:import", CONTEXT);
	}

	private boolean hasCorrectType(Node fileImport) {
		return isImportTypedWithinThisFiles(fileImport,
				fileHandler.getAllWsdls())
				|| isImportTypedWithinThisFiles(fileImport,
						fileHandler.getAllXsds());
	}

	private boolean isImportTypedWithinThisFiles(Node fileImport,
			List<DocumentEntry> allFiles) {
		for (DocumentEntry documentEntry : allFiles) {
			String importType = new NodeHelper(fileImport)
					.getAttribute("importType");
			if (isCorrespondingFile(fileImport, documentEntry)) {
				if (importType.equals(documentEntry.getNamespace())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCorrespondingFile(Node fileImport,
			DocumentEntry documentEntry) {
		String location = new ImportElement(fileImport)
				.getAbsoluteLocation(fileHandler.getAbsoluteBpelFolder());
		return documentEntry.getFilePath().equals(location);
	}

	@Override
	public int getSaNumber() {
		return 13;
	}
}
