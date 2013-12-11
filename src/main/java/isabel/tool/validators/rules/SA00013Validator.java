package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.bpel.ImportElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import java.nio.file.Path;
import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class SA00013Validator extends Validator {

	public SA00013Validator(ProcessContainer files,
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
				fileHandler.getWsdls())
				|| isImportTypedWithinThisFiles(fileImport,
				fileHandler.getXsds());
	}

	private boolean isImportTypedWithinThisFiles(Node fileImport,
	                                             List<XmlFile> allFiles) {
		for (XmlFile xmlFile : allFiles) {
			String importType = new NodeHelper(fileImport)
					.getAttribute("importType");
			if (isCorrespondingFile(fileImport, xmlFile)) {
				if (importType.equals(xmlFile.getNamespace())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isCorrespondingFile(Node fileImport,
	                                    XmlFile xmlFile) {
		Path location = new ImportElement(fileImport)
				.getAbsoluteLocation(fileHandler.getAbsoluteBpelFolder());
		return xmlFile.getFilePath().equals(location);
	}

	@Override
	public int getSaNumber() {
		return 13;
	}
}
