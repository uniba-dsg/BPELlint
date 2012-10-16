package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.HashSet;
import java.util.Set;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00044Validator extends Validator {

	public SA00044Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes correlationSetNames = fileHandler
				.getBpel()
				.getDocument()
				.query("//bpel:correlationSets/bpel:correlationSet/@name",
						CONTEXT);
		String fileName = fileHandler.getBpel().getFilePath();

		Set<String> occurringNames = new HashSet<>();
		for (Node currentNode : correlationSetNames) {
			if (!occurringNames.add(currentNode.toXML())) {
				addViolation(fileName, currentNode, 1);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 44;
	}
}
