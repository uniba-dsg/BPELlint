package bpellint.tool.validators.result;


import validator.Violation;
import validator.Warning;

import java.nio.file.Path;

public interface ValidationCollector {

    void addWarning(Warning warning);

    void addViolation(Violation violation);

    void addFile(Path s);
}
