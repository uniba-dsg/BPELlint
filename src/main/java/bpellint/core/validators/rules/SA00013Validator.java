package bpellint.core.validators.rules;


import java.nio.file.Path;
import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.XmlFile;
import bpellint.core.model.bpel.ImportElement;

public class SA00013Validator extends Validator {

    public SA00013Validator(ProcessContainer files,
                            SimpleValidationResult violationCollector) {
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
