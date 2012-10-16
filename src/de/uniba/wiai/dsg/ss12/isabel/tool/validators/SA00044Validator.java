package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

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
		for (int i = 0; i < correlationSetNames.size(); i++) {
			Node currentNode = correlationSetNames.get(i);
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
