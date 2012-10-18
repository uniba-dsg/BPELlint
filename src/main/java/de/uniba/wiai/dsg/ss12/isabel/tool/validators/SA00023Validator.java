package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00023Validator extends Validator {

	private static final int UNIQUE_IN_SCOPE = 2;
	private static final int UNIQUE_IN_PROCESS = 1;

	public SA00023Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes processVariableNames = fileHandler
				.getBpel()
				.getDocument()
				.query("//bpel:process/bpel:variables/bpel:variable/@name",
						CONTEXT);

		checkForDuplicates(processVariableNames, UNIQUE_IN_PROCESS);

		Nodes scopes = fileHandler.getBpel().getDocument()
				.query("//bpel:scope", CONTEXT);
		for (Node scope : scopes) {
			Nodes variableNames = scope.query(
					"bpel:variables/bpel:variable/@name", CONTEXT);
			checkForDuplicates(variableNames, UNIQUE_IN_SCOPE);
		}
	}

	private void checkForDuplicates(Nodes nodesToCheck, int errorType) {
		if (nodesToCheck.size() > 1) {
			for (int i = 0; i < nodesToCheck.size(); i++) {
				Node currentNode = nodesToCheck.get(i);

				for (int j = i + 1; j < nodesToCheck.size(); j++) {
					if (nodesToCheck.get(j).getValue()
							.equals(currentNode.getValue())) {
						addViolation(currentNode, errorType);
					}
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 23;
	}

}
