package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00007Validator extends Validator {

	public SA00007Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		String fileName = fileHandler.getBpel().getFilePath();
		Nodes compensateScopes = fileHandler.getBpel().getDocument()
				.query("//bpel:compensateScope", CONTEXT);

		for (Node compensateScope : compensateScopes) {
			Nodes ancestors = compensateScope.query("ancestor::*", CONTEXT);

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
				addViolation(fileName, compensateScope, 1);
			}
		}

		return valid;
	}

	@Override
	public int getSaNumber() {
		return 7;
	}

}
