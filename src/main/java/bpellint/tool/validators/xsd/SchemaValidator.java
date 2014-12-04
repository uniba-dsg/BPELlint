package bpellint.tool.validators.xsd;


import java.nio.file.Path;

import validator.ValidationException;

public class SchemaValidator implements SchemaValidatorFacade {

    public static SchemaValidatorFacade NULL_OBJECT = new SchemaValidatorFacade() {

        @Override
        public void validateBpel(Path file) throws ValidationException {

        }

        @Override
        public void validateWsdl(Path file) throws ValidationException {

        }

        @Override
        public void validateXsd(Path file) throws ValidationException {

        }

    };

	private static SchemaValidatorFacade schemaValidatorFacade;

	public static SchemaValidatorFacade getInstance() throws ValidationException {
		if (schemaValidatorFacade == null) {
			schemaValidatorFacade = new SchemaValidator();
		}
		return schemaValidatorFacade;
	}

	protected BPELValidator bpelValidator;
    protected WSDLValidator wsdlValidator;
    protected XSDValidator xsdValidator;

	private SchemaValidator() throws ValidationException {
		xsdValidator = new XSDValidator();
		wsdlValidator = new WSDLValidator();
		bpelValidator = new BPELValidator();
	}

	@Override
    public void validateBpel(Path file) throws ValidationException {
		bpelValidator.validate(file);
	}

	@Override
    public void validateWsdl(Path file) throws ValidationException {
		wsdlValidator.validate(file);
	}

	@Override
    public void validateXsd(Path file) throws ValidationException {
		xsdValidator.validate(file);
	}

}
