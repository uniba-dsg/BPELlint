package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.bpel.ImportElement;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00013Validator extends Validator {

	public SA00013Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
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
			String importType = new NodeHelper(fileImport).getAttribute("importType");
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
		String location = getAbsoluteFilePath(fileImport);
		return documentEntry.getFilePath().equals(location);
	}

	private String getAbsoluteFilePath(Node fileImport) {
		try {
			return new ImportElement(fileImport).getAbsoluteLocation(fileHandler.getAbsoluteBpelFolder());
		} catch (IOException e) {
			Logger.error(e);
			throw new IllegalStateException(e);
		}
	}

	@Override
	public int getSaNumber() {
		return 13;
	}
}
