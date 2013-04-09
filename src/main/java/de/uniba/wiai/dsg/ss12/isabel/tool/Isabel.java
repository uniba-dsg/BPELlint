package de.uniba.wiai.dsg.ss12.isabel.tool;

import java.nio.file.Files;
import java.nio.file.Paths;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.SimpleValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.XmlFileLoader;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules.ValidatorsHandler;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.xsd.SchemaValidator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.xsd.XMLValidator;

/**
 * Isabel, a static analyzer for BPEL processes
 * 
 * @author David Bimamisa, Christian Preißinger, Stephan Schuberth; Project
 *         Distributed Systems Group - University of Bamberg - SS 2012
 */
public class Isabel {

	/**
	 * @param bpelPath
	 *            The BPEL file of the process, which should be analyzed
	 *            statically.
	 * @return A ValidationCollector, which is a collection of occurred
	 *         rule-violations.
	 * @throws ValidationException
	 *             If loading is not working correctly
	 */
	public ValidationResult validate(String bpelPath)
			throws ValidationException {
		if (bpelPath == null) {
			throw new ValidationException("Path is no BPEL file");
		}
		if (!Files.exists(Paths.get(bpelPath))) {
			throw new ValidationException("File " + bpelPath
					+ " does not exist");
		}

		// validate well-formedness and correct schema of bpel file
		new XMLValidator().validate(bpelPath);
		SchemaValidator schemaValidator = SchemaValidator.newInstance();
		schemaValidator.validateBpel(bpelPath);

		// load files
		BpelProcessFiles bpelProcessFiles = new XmlFileLoader()
				.loadAllProcessFiles(bpelPath);

		// validate XML Schema
		validateWsdlAndXsdFiles(bpelProcessFiles);

		// validate SA rules
		return validateAgainstSARules(bpelProcessFiles);
	}

	private void validateWsdlAndXsdFiles(BpelProcessFiles bpelProcessFiles)
			throws ValidationException {
		SchemaValidator schemaValidator = SchemaValidator.newInstance();

		for (DocumentEntry xsdDocumentEntry : bpelProcessFiles.getAllXsds()) {
			// do not validate XMLSchema as this does not work somehow
			if (xsdDocumentEntry.getFilePath().equals("")) {
				continue;
			}
			schemaValidator.validateXsd(xsdDocumentEntry.getFilePath());
		}

		for (DocumentEntry wsdlDocumentEntry : bpelProcessFiles.getAllWsdls()) {
			schemaValidator.validateWsdl(wsdlDocumentEntry.getFilePath());
		}
	}

	private SimpleValidationResult validateAgainstSARules(
			BpelProcessFiles bpelProcessFiles) {
		SimpleValidationResult validationResult = new SimpleValidationResult();
		ValidatorsHandler validators = new ValidatorsHandler(bpelProcessFiles,
				validationResult);
		validators.validate();
		return validationResult;
	}

}
