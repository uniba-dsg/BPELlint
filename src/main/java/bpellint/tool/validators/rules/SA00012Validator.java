package bpellint.tool.validators.rules;


import java.nio.file.Path;
import java.util.List;

import bpellint.model.ProcessContainer;
import bpellint.model.XmlFile;
import bpellint.model.bpel.ImportElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00012Validator extends Validator {

    public SA00012Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        List<ImportElement> imports = processContainer.getAllImports();

        List<XmlFile> allWsdls = processContainer.getDirectlyImportedWsdls();
        List<XmlFile> allXsds = processContainer.getDirectlyImportedXsds();

        for (ImportElement node : imports) {
            boolean importedFileIsValid = isValidFile(allWsdls, node) && isValidFile(allXsds, node);

            if (!importedFileIsValid) {
                addViolation(node);
            }
        }
    }

    private boolean isValidFile(List<XmlFile> xmlFileList, ImportElement node) {

        boolean validFile = true;

        if (!node.hasNamespaceAttribute()) {

            for (XmlFile xmlFile : xmlFileList) {

                Path canonicalPath = processContainer.getAbsoluteBpelFolder().resolve(node.getLocation()).toAbsolutePath().normalize();
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