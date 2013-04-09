package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;

public class SA00023Validator extends Validator {

	public SA00023Validator(BpelProcessFiles files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node variablesContainer : getVariablesContainer()) {
			Set<String> names = new HashSet<>();
			for (Node variable : variablesContainer.query("bpel:variable",
					CONTEXT)) {
				String name = new NodeHelper(variable).getAttribute("name");
				if (names.contains(name)) {
					addViolation(variable);
				} else {
					names.add(name);
				}
			}
		}
	}

	private Nodes getVariablesContainer() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:variables", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 23;
	}

}
