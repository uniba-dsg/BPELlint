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
	public boolean validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes compensates = fileHandler.getBpel().getDocument()
				.query("//bpel:compensate", CONTEXT);

		for (Node compensate : compensates) {
			Nodes ancestors = compensate.query("ancestor::*", CONTEXT);

			boolean foundCorrespondingFTCHandler = false;
			for (Node node : ancestors) {
				Nodes faultHandlers = node.query("bpel:faultHandlers", CONTEXT);
				Nodes compensationHandlers = node.query(
						"bpel:compensationHandler", CONTEXT);
				Nodes terminationHandlers = node.query(
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

		return valid;
	}

	@Override
	public int getSaNumber() {
		return 8;
	}

}
