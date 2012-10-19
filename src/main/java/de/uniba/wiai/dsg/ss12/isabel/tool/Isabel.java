package de.uniba.wiai.dsg.ss12.isabel.tool;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.SimpleValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.XmlFileLoader;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorsHandler;

import java.nio.file.Files;
import java.nio.file.Paths;

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
	 * @return A ValidationResult, which is a collection of occurred
	 *         rule-violations.
	 * @throws ValidationException
	 *             If loading is not working correctly
	 */
	public ValidationResult validate(String bpelPath)
			throws ValidationException {
		if (bpelPath == null) {
			throw new ValidationException("Path is no BPEL file");
		}
        if (!Files.exists(Paths.get(bpelPath))){
            throw new ValidationException("File " + bpelPath + " does not exist");
        }

		BpelProcessFiles bpelProcessFiles = new XmlFileLoader().loadAllProcessFiles(bpelPath);
		SimpleValidationResult validationResult = new SimpleValidationResult();
		ValidatorsHandler validators = new ValidatorsHandler(bpelProcessFiles, validationResult);
		validators.validate();

		return validationResult;
	}

}
