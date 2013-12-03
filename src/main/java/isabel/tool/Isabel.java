package isabel.tool;

import isabel.tool.impl.SimpleValidationResult;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.ProcessContainerLoader;
import isabel.tool.imports.XmlFile;
import isabel.tool.validators.rules.ValidatorsHandler;
import isabel.tool.validators.xsd.SchemaValidator;
import isabel.tool.validators.xsd.XMLValidator;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Isabel, a static analyzer for BPEL processes
 *
 * @author David Bimamisa, Christian Preißinger, Stephan Schuberth; Project
 *         Distributed Systems Group - University of Bamberg - SS 2012
 */
public class Isabel {

    /**
     * @param bpelPath The BPEL file of the process, which should be analyzed
     *                 statically.
     * @return A ValidationCollector, which is a collection of occurred
     * rule-violations.
     * @throws ValidationException If loading is not working correctly
     */
    public ValidationResult validate(Path bpelPath)
            throws ValidationException {
        if (bpelPath == null) {
            throw new ValidationException("Path is no BPEL file");
        }
        if (!Files.exists(bpelPath)) {
            throw new ValidationException("File " + bpelPath
                    + " does not exist");
        }

        // validate well-formedness and correct schema of bpel file
        new XMLValidator().validate(bpelPath);
        SchemaValidator schemaValidator = SchemaValidator.newInstance();
        schemaValidator.validateBpel(bpelPath);

        // load files
        ProcessContainer processContainer = new ProcessContainerLoader().load(bpelPath);

        // validate XML Schema
        validateWsdlAndXsdFiles(processContainer);

        // validate SA rules
        return validateAgainstSARules(processContainer);
    }

    private void validateWsdlAndXsdFiles(ProcessContainer processContainer) throws ValidationException {
        SchemaValidator schemaValidator = SchemaValidator.newInstance();

        for (XmlFile xsdXmlFile : processContainer.getXsds()) {
            // do not validate XMLSchema as this does not work somehow
            if (xsdXmlFile.getFilePath().equals(Paths.get(""))) {
                continue;
            }
            schemaValidator.validateXsd(xsdXmlFile.getFilePath());
        }

        for (XmlFile wsdlXmlFile : processContainer.getWsdls()) {
            schemaValidator.validateWsdl(wsdlXmlFile.getFilePath());
        }
    }

    private SimpleValidationResult validateAgainstSARules(
            ProcessContainer processContainer) {
        SimpleValidationResult validationResult = new SimpleValidationResult();
        ValidatorsHandler validators = new ValidatorsHandler(processContainer,
                validationResult);
        validators.validate();
        return validationResult;
    }

}
