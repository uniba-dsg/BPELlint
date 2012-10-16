package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import java.io.File;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00013Validator extends Validator {

	public SA00013Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		String filePath = fileHandler.getBpel().getFilePath();
		Nodes imports = getAllImports();

		for (Node node : imports) {
			if (!hasCorrectType(node)) {
				addViolation(filePath, node, 1);
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
			String importType = getAttributeValue(fileImport.query(
					"@importType", CONTEXT));

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
		String location = getAttributeValue(fileImport.query("@location",
				CONTEXT));
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
