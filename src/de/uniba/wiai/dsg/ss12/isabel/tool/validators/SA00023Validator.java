package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00023Validator extends Validator {

	private String fileName;

	public SA00023Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		this.fileName = fileHandler.getBpel().getFilePath();
		Nodes processVariableNames = fileHandler
				.getBpel()
				.getDocument()
				.query("//bpel:process/bpel:variables/bpel:variable/@name",
						CONTEXT);

		checkForDuplicates(processVariableNames, 1);

		Nodes scopes = fileHandler.getBpel().getDocument()
				.query("//bpel:scope", CONTEXT);
		for (Node scope : scopes) {
			Nodes variableNames = scope.query(
					"bpel:variables/bpel:variable/@name", CONTEXT);
			checkForDuplicates(variableNames, 2);
		}
	}

	private void checkForDuplicates(Nodes nodesToCheck, int errorType) {
		if (nodesToCheck.size() > 1) {
			for (int i = 0; i < nodesToCheck.size(); i++) {
				Node currentNode = nodesToCheck.get(i);

				for (int j = i + 1; j < nodesToCheck.size(); j++) {
					if (nodesToCheck.get(j).getValue()
							.equals(currentNode.getValue())) {
						addViolation(fileName, currentNode, errorType);
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
