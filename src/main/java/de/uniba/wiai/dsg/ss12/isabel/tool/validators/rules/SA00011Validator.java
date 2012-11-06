package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00011Validator extends Validator {

	public SA00011Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes imports = fileHandler.getBpel().getDocument()
				.query("//bpel:import", CONTEXT);

		List<DocumentEntry> allWsdls = fileHandler.getAllWsdls();
		List<DocumentEntry> allXsds = fileHandler.getAllXsds();

		for (Node node : imports) {
			Logger.debug("Checking <import> Element " + node);
			boolean validFile = isValidFile(allWsdls, node)
					|| isValidFile(allXsds, node);

			if (!validFile) {
				addViolation(node);
			}
		}
	}

	private boolean isValidFile(List<DocumentEntry> documentEntryList, Node node) {

		boolean validFile = false;

		for (DocumentEntry documentEntry : documentEntryList) {

			String namespace = new NodeHelper(node).getAttribute("namespace");
			String location = new NodeHelper(node).getAttribute("location");
			Logger.debug("<import>@namespace = " + namespace);
			Logger.debug("<import>@location = " + location);
			File path = new File(fileHandler.getAbsoluteBpelFilePath() + "/"
					+ location);
			Logger.debug("path based on <import>@location = " + path);

			Logger.debug("Comparing " + documentEntry.getFilePath() + " with " + path.getAbsolutePath());
			if (documentEntry.getFilePath().equals(path.getAbsolutePath())) {

				if (namespace.equals(documentEntry.getTargetNamespace())) {
					validFile = true;
				}
			}
		}
		return validFile;
	}

	@Override
	public int getSaNumber() {
		return 11;
	}
}