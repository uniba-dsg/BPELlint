package bpellint.tool.validators.xsd;


import java.nio.file.Path;

import validator.ValidationException;

public interface SchemaValidatorFacade {
    void validateBpel(Path file) throws ValidationException;
    void validateWsdl(Path file) throws ValidationException;
    void validateXsd(Path file) throws ValidationException;
}
