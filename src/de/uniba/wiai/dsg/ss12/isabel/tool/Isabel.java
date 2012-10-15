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

	private final XmlFileLoader fileLoader = new XmlFileLoader();
	private final ViolationCollector violationCollector = new IsabelViolationCollector();

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
		if (bpelPath == null) {
			throw new ValidationException("Path is no BPEL file");
		}

		BpelProcessFiles bpelProcessFiles = fileLoader.loadAllProcessFiles(bpelPath);
		ValidatorsHandler validators = new ValidatorsHandler(bpelProcessFiles, violationCollector);
		validators.validate();

		return violationCollector;
	}

}
