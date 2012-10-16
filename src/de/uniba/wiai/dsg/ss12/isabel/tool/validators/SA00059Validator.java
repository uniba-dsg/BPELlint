package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00059Validator extends Validator {

	public SA00059Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		String fileName = fileHandler.getBpel().getFilePath();
		Nodes replies = fileHandler.getBpel().getDocument()
				.query("//bpel:reply", CONTEXT);

		for (Node reply : replies) {

			Nodes toPartsSet = reply.query("bpel:toParts", CONTEXT);
			Nodes variableSet = reply.query("@variable", CONTEXT);
			if ((toPartsSet.size() > 0) && (variableSet.size() > 0)) {
				addViolation(fileName, reply, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}