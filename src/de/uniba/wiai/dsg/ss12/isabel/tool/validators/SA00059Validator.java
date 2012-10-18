package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00059Validator extends Validator {

	public SA00059Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes replies = fileHandler.getBpel().getDocument()
				.query("//bpel:reply", CONTEXT);

		for (Node reply : replies) {

			Nodes toPartsSet = reply.query("bpel:toParts", CONTEXT);
			Nodes variableSet = reply.query("@variable", CONTEXT);
			if ((toPartsSet.size() > 0) && (variableSet.size() > 0)) {
				addViolation(reply);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}