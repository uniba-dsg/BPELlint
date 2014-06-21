package bpellint.tool.validators.rules;


import java.nio.file.Path;
import java.util.List;

import bpellint.model.ProcessContainer;
import bpellint.model.XmlFile;
import bpellint.model.bpel.ImportElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00013Validator extends Validator {

    public SA00013Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (ImportElement importNode : processContainer.getAllImports()) {
            if (!hasCorrectType(importNode)) {
                addViolation(importNode);
            }
        }
    }

    private boolean hasCorrectType(ImportElement fileImport) {
        return isImportTypedWithinThisFiles(fileImport,
                processContainer.getWsdls())
                || isImportTypedWithinThisFiles(fileImport,
                processContainer.getXsds());
    }

    private boolean isImportTypedWithinThisFiles(ImportElement fileImport,
                                                 List<XmlFile> allFiles) {
        for (XmlFile xmlFile : allFiles) {
            if (isCorrespondingFile(fileImport, xmlFile.getFilePath())) {
                if (fileImport.getImportType().equals(xmlFile.getNamespace())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCorrespondingFile(ImportElement fileImport, Path xmlFile) {
        Path location = fileImport.getAbsoluteLocation(processContainer.getAbsoluteBpelFolder());
        return xmlFile.equals(location);
    }

    @Override
    public int getSaNumber() {
        return 13;
    }
}
