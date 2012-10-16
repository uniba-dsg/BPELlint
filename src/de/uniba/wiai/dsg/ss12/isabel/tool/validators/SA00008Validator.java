package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00008Validator extends Validator {

	public SA00008Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes compensates = fileHandler.getBpel().getDocument()
				.query("//bpel:compensate", CONTEXT);

		for (Node compensate : compensates) {
			Nodes ancestors = compensate.query("ancestor::*", CONTEXT);

			boolean foundCorrespondingFTCHandler = false;
			for (Node ancestor : ancestors) {
				Nodes faultHandlers = ancestor.query("bpel:faultHandlers", CONTEXT);
				Nodes compensationHandlers = ancestor.query(
						"bpel:compensationHandler", CONTEXT);
				Nodes terminationHandlers = ancestor.query(
						"bpel:terminationHandlers", CONTEXT);

				if (faultHandlers.size() > 0 || compensationHandlers.size() > 0
						|| terminationHandlers.size() > 0) {
					foundCorrespondingFTCHandler = true;
				}
			}
			if (!foundCorrespondingFTCHandler) {
				addViolation(fileName, compensate, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 8;
	}

}
