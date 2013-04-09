package isabel.tool.validators.xsd;

import isabel.tool.ValidationException;

public class SchemaValidator {

	private static SchemaValidator schemaValidator;

	public static SchemaValidator newInstance() throws ValidationException {
		if (schemaValidator == null) {
			schemaValidator = new SchemaValidator();
		}
		return schemaValidator;
	}

	private BPELValidator bpelValidator;
	private WSDLValidator wsdlValidator;
	private XSDValidator xsdValidator;

	private SchemaValidator() throws ValidationException {
		xsdValidator = new XSDValidator();
		wsdlValidator = new WSDLValidator();
		bpelValidator = new BPELValidator();
	}

	public void validateBpel(String file) throws ValidationException {
		bpelValidator.validate(file);
	}

	public void validateWsdl(String file) throws ValidationException {
		wsdlValidator.validate(file);
	}

	public void validateXsd(String file) throws ValidationException {
		xsdValidator.validate(file);
	}
}
