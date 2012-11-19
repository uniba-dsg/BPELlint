package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.bpel.ImportElement;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00011Validator extends Validator {

	public SA00011Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		List<DocumentEntry> allWsdls = fileHandler.getAllWsdls();
		List<DocumentEntry> allXsds = fileHandler.getAllXsds();

		for (Node node : fileHandler.getImports()) {
			Logger.debug("Checking <import> Element " + node.toXML());
			boolean validFile = isValidFile(allWsdls, node)
					|| isValidFile(allXsds, node);

			if (!validFile) {
				addViolation(node);
			}
		}
	}

	private boolean isValidFile(List<DocumentEntry> documentEntryList, Node node) {
		Logger.debug("Validating Namespace for " + node.toXML());

		ImportElement importElement = new ImportElement(node);

		String absolutePath = null;
		try {
			absolutePath = importElement.getAbsoluteLocation(this.fileHandler.getAbsoluteBpelFolder());
		} catch (IOException e) {
			Logger.error(e);
			return false;
		}

		for (DocumentEntry documentEntry : documentEntryList) {

			String filePath = documentEntry.getFilePath();
			Logger.debug("Comparing [" + filePath + "] with [" + absolutePath + "]");
			if (filePath.equals(absolutePath)) {

				Logger.debug("Comparing [" + importElement.getNamespace() + "] with [" + documentEntry.getTargetNamespace() + "]");
				if (importElement.getNamespace().equals(documentEntry.getTargetNamespace())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getSaNumber() {
		return 11;
	}
}