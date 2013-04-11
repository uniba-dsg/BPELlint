package isabel.tool.validators.rules;

import isabel.tool.helper.bpel.ImportElement;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;
import nu.xom.Node;
import org.pmw.tinylog.Logger;

import java.util.List;

public class SA00011Validator extends Validator {

	public SA00011Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		List<XmlFile> allWsdls = fileHandler.getAllWsdls();
		List<XmlFile> allXsds = fileHandler.getAllXsds();

		for (Node node : fileHandler.getImports()) {
			Logger.debug("Checking <import> Element " + node.toXML());
			boolean validFile = isValidFile(allWsdls, node)
					|| isValidFile(allXsds, node);

			if (!validFile) {
				addViolation(node);
			}
		}
	}

	private boolean isValidFile(List<XmlFile> xmlFileList, Node node) {
		Logger.debug("Validating Namespace for " + node.toXML());

		ImportElement importElement = new ImportElement(node);

		String absolutePath = importElement
				.getAbsoluteLocation(this.fileHandler.getAbsoluteBpelFolder());

		for (XmlFile xmlFile : xmlFileList) {
			String filePath = xmlFile.getFilePath();
			Logger.debug("Comparing [" + filePath + "] with [" + absolutePath
					+ "]");
			if (filePath.equals(absolutePath)) {

				Logger.debug("Comparing [" + importElement.getNamespace()
						+ "] with [" + xmlFile.getTargetNamespace() + "]");
				if (importElement.getNamespace().equals(
						xmlFile.getTargetNamespace())) {
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