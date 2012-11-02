package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;

import java.io.File;
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
				if (importType.equals(getDefaultNamespace(documentEntry))) {
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
		String location = new NodeHelper(fileImport).getAttribute("location");
		File path = new File(fileHandler.getAbsoluteBpelFilePath() + "/"
				+ location);

		return path.getAbsolutePath();
	}

	private String getDefaultNamespace(DocumentEntry documentEntry) {
		return toElement(
				documentEntry.getDocument().query("/*", CONTEXT).get(0))
				.getNamespaceURI();
	}

	@Override
	public int getSaNumber() {
		return 13;
	}
}