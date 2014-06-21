package bpellint.tool;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import bpellint.model.ProcessContainer;
import bpellint.model.XmlFile;
import bpellint.tool.imports.ImportException;
import bpellint.tool.imports.ProcessContainerLoader;
import bpellint.tool.validators.ValidationException;
import bpellint.tool.validators.ValidatorsHandler;
import bpellint.tool.validators.result.ValidationResult;
import bpellint.tool.validators.xsd.SchemaValidator;
import bpellint.tool.validators.xsd.SchemaValidatorFacade;
import bpellint.tool.validators.xsd.XMLValidator;

/**
 * BpelLint, a static analyzer for BPEL processes
 */
public class BpelLint {

    private SchemaValidatorFacade schemaValidatorFacade;

    public BpelLint() throws ValidationException {
        schemaValidatorFacade = SchemaValidator.getInstance();
    }

    private BpelLint(SchemaValidatorFacade schemaValidatorFacade) {
        this.schemaValidatorFacade = schemaValidatorFacade;
    }

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

        schemaValidatorFacade.validateBpel(bpelPath);

        // load files
        ProcessContainer processContainer = loadProcessContainer(bpelPath);

        // validate XML Schema
        validateWsdlAndXsdFiles(processContainer);

        // validate SA rules
        return validateAgainstSARules(processContainer);
    }

    private ProcessContainer loadProcessContainer(Path bpelPath) throws ValidationException {
        try {
            return new ProcessContainerLoader().load(bpelPath);
        } catch (ImportException e) {
            throw new ValidationException("could not import process", e);
        }
    }

    private void validateWsdlAndXsdFiles(ProcessContainer processContainer) throws ValidationException {
        for (XmlFile xsdXmlFile : processContainer.getXsds()) {
            // do not validate XMLSchema as this does not work somehow
            if (xsdXmlFile.getFilePath().equals(Paths.get(""))) {
                continue;
            }
            schemaValidatorFacade.validateXsd(xsdXmlFile.getFilePath());
        }

        for (XmlFile wsdlXmlFile : processContainer.getWsdls()) {
            schemaValidatorFacade.validateWsdl(wsdlXmlFile.getFilePath());
        }
    }

    private ValidationResult validateAgainstSARules(
            ProcessContainer processContainer) {
        return new ValidatorsHandler(processContainer).validate();
    }

    public static BpelLint buildWithoutSchemaValidation() {
        return new BpelLint(SchemaValidator.NULL_OBJECT);
    }

}
