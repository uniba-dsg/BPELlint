package de.uniba.wiai.dsg.ss12.isabel.tool;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.XmlFileLoader;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.IsabelViolationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorsHandler;

/**
 * Isabel, a static analyzer for BPEL processes
 * 
 * @author David Bimamisa, Christian Preißinger, Stephan Schuberth; Project
 *         Distributed Systems Group - University of Bamberg - SS 2012
 */
public class Isabel {

	private String bpelFilePath;
	private XmlFileLoader fileLoader = new XmlFileLoader();
	private ValidatorsHandler validators;
	private final ViolationCollector violationCollector = new IsabelViolationCollector();
	private BpelProcessFiles bpelProcessFiles;

	/**
	 * @param bpelPath
	 *            The BPEL file of the process, which should be analyzed
	 *            statically.
	 * @return A ViolationCollector, which is a collection of occurred
	 *         rule-violations.
	 * @throws ValidationException
	 *             If loading is not working correctly
	 */
	public ViolationCollector validate(String bpelPath)
			throws ValidationException {
		if (!isCallArgBpelFile(bpelPath)) {
			throw new ValidationException("Path is no BPEL file");
		}

		loadAllProcessFiles();
		createValidators();
		validate();

		return violationCollector;
	}

	private boolean isCallArgBpelFile(String path) {
		if (path == null) {
			return false;
		}

		bpelFilePath = path;
		return true;
	}

	private void loadAllProcessFiles() throws ValidationException {
		bpelProcessFiles = fileLoader.loadAllProcessFiles(bpelFilePath);
	}

	private void createValidators() {
		validators = new ValidatorsHandler(bpelProcessFiles, violationCollector);
	}

	private void validate() {
		validators.validate();
	}
}
