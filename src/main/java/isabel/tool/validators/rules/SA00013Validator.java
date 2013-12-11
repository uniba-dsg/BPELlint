package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import isabel.model.bpel.ImportElement;
import isabel.tool.impl.ValidationCollector;

import java.nio.file.Path;
import java.util.List;

public class SA00013Validator extends Validator {

    public SA00013Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        for (ImportElement importNode : fileHandler.getAllImports()) {
            if (!hasCorrectType(importNode)) {
                addViolation(importNode);
            }
        }
    }

    private boolean hasCorrectType(ImportElement fileImport) {
        return isImportTypedWithinThisFiles(fileImport,
                fileHandler.getWsdls())
                || isImportTypedWithinThisFiles(fileImport,
                fileHandler.getXsds());
    }

    private boolean isImportTypedWithinThisFiles(ImportElement fileImport,
                                                 List<XmlFile> allFiles) {
        for (XmlFile xmlFile : allFiles) {
            if (isCorrespondingFile(fileImport, xmlFile)) {
                if (fileImport.getImportType().equals(xmlFile.getNamespace())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isCorrespondingFile(ImportElement fileImport,
                                        XmlFile xmlFile) {
        Path location = fileImport.getAbsoluteLocation(fileHandler.getAbsoluteBpelFolder());
        return xmlFile.getFilePath().equals(location);
    }

    @Override
    public int getSaNumber() {
        return 13;
    }
}
