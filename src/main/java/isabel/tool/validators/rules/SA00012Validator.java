package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import java.io.File;
import java.util.List;

import static isabel.tool.impl.Standards.CONTEXT;

public class SA00012Validator extends Validator {

	public SA00012Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes imports = fileHandler.getBpel().getDocument()
				.query("//bpel:import", CONTEXT);

		List<XmlFile> allWsdls = fileHandler.getAllWsdls();
		List<XmlFile> allXsds = fileHandler.getAllXsds();

		for (Node node : imports) {
			boolean importedFileIsValid = isValidFile(allWsdls, node)
					&& isValidFile(allXsds, node);

			if (!importedFileIsValid) {
				addViolation(node);
			}
		}
	}

	private boolean isValidFile(List<XmlFile> xmlFileList, Node node) {

		boolean validFile = true;

		if (new NodeHelper(node).hasNoAttribute("namespace")) {

			for (XmlFile xmlFile : xmlFileList) {

				String location = new NodeHelper(node).getAttribute("location");
				File path = new File(fileHandler.getAbsoluteBpelFolder() + "/"
						+ location);

				if (xmlFile.getFilePath().equals(path.getAbsolutePath())) {

					if (!"".equals(xmlFile.getTargetNamespace())) {
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