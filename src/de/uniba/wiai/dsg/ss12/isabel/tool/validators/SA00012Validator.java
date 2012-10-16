package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import java.io.File;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00012Validator extends Validator {

	public SA00012Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();

		Nodes imports = fileHandler.getBpel().getDocument()
				.query("//bpel:import", CONTEXT);

		List<DocumentEntry> allWsdls = fileHandler.getAllWsdls();
		List<DocumentEntry> allXsds = fileHandler.getAllXsds();

		for (Node node : imports) {
			boolean importedFileIsValid = isValidFile(allWsdls, node)
					&& isValidFile(allXsds, node);

			if (!importedFileIsValid) {
				addViolation(fileName, node, 1);
			}
		}
	}

	private boolean isValidFile(List<DocumentEntry> documentEntryList, Node node) {

		boolean validFile = true;

		if (new NodeHelper(node).hasNoAttribute("namespace")) {

			for (DocumentEntry documentEntry : documentEntryList) {

				String location = new NodeHelper(node).getAttributeByName("location");
				File path = new File(fileHandler.getAbsoluteBpelFilePath() + "/"
						+ location);

				if (documentEntry.getFilePath().equals(path.getAbsolutePath())) {

					if (!"".equals(documentEntry.getTargetNamespace())) {
						validFile = false;
					}
				}
			}
		}
		return validFile;
	}

	@Override
	public int getSaNumber() {
		return 12;
	}
}