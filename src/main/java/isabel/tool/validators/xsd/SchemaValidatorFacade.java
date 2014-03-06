package isabel.tool.validators.xsd;

import isabel.tool.validators.ValidationException;

import java.nio.file.Path;

public interface SchemaValidatorFacade {
    void validateBpel(Path file) throws ValidationException;
    void validateWsdl(Path file) throws ValidationException;
    void validateXsd(Path file) throws ValidationException;
}
