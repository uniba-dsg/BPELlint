package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;

public class SA00044Validator extends Validator {

	public SA00044Validator(BpelProcessFiles files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node correlationSetContainer : getCorrelationSetContainers()) {
			Set<String> names = new HashSet<>();
			for (Node correlationSet : correlationSetContainer.query(
					"bpel:correlationSet", CONTEXT)) {
				String name = new NodeHelper(correlationSet)
						.getAttribute("name");

				if (names.contains(name)) {
					addViolation(correlationSet);
				} else {
					names.add(name);
				}
			}
		}
	}

	private Nodes getCorrelationSetContainers() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:correlationSets", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 44;
	}
}
