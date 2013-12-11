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

public class SA00012Validator extends Validator {

    public SA00012Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        List<ImportElement> imports = fileHandler.getAllImports();

        List<XmlFile> allWsdls = fileHandler.getDirectlyImportedWsdls();
        List<XmlFile> allXsds = fileHandler.getDirectlyImportedXsds();

        for (ImportElement node : imports) {
            boolean importedFileIsValid = isValidFile(allWsdls, node)
                    && isValidFile(allXsds, node);

            if (!importedFileIsValid) {
                addViolation(node);
            }
        }
    }

    private boolean isValidFile(List<XmlFile> xmlFileList, ImportElement node) {

        boolean validFile = true;

        if (node.hasNamespaceAttribute()) {

            for (XmlFile xmlFile : xmlFileList) {

                Path canonicalPath = fileHandler.getAbsoluteBpelFolder().resolve(node.getLocation()).toAbsolutePath().normalize();
                if (xmlFile.getFilePath().equals(canonicalPath)) {

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