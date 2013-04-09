package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;

public class SA00076Validator extends Validator {

	public SA00076Validator(BpelProcessFiles files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes forEachLoops = fileHandler.getBpel().getDocument()
				.query("//bpel:forEach", Standards.CONTEXT);
		for (Node forEach : forEachLoops) {
			NodeHelper forEachHelper = new NodeHelper(forEach);

			Nodes variables = forEach.query(
					"bpel:scope/bpel:variables/bpel:variable",
					Standards.CONTEXT);
			for (Node variable : variables) {
				NodeHelper variableHelper = new NodeHelper(variable);

				if (forEachHelper.getAttribute("counterName").equals(
						variableHelper.getAttribute("name"))) {
					addViolation(variable);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 76;
	}
}
