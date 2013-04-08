package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;

import java.io.File;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00012Validator extends Validator {

	public SA00012Validator(BpelProcessFiles files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes imports = fileHandler.getBpel().getDocument()
				.query("//bpel:import", CONTEXT);

		List<DocumentEntry> allWsdls = fileHandler.getAllWsdls();
		List<DocumentEntry> allXsds = fileHandler.getAllXsds();

		for (Node node : imports) {
			boolean importedFileIsValid = isValidFile(allWsdls, node)
					&& isValidFile(allXsds, node);

			if (!importedFileIsValid) {
				addViolation(node);
			}
		}
	}

	private boolean isValidFile(List<DocumentEntry> documentEntryList, Node node) {

		boolean validFile = true;

		if (new NodeHelper(node).hasNoAttribute("namespace")) {

			for (DocumentEntry documentEntry : documentEntryList) {

				String location = new NodeHelper(node).getAttribute("location");
				File path = new File(fileHandler.getAbsoluteBpelFolder() + "/"
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