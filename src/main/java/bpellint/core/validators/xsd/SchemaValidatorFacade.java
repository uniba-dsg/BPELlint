package bpellint.core.validators.xsd;


import java.nio.file.Path;

import api.ValidationException;

public interface SchemaValidatorFacade {
    void validateBpel(Path file) throws ValidationException;
    void validateWsdl(Path file) throws ValidationException;
    void validateXsd(Path file) throws ValidationException;
}
