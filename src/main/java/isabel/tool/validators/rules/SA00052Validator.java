package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;

public class SA00052Validator extends Validator {
	public SA00052Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes invokeNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokeNodes) {
			Nodes fromParts = invoke.query("bpel:fromParts", CONTEXT);
			String outputVariable = new NodeHelper(invoke)
					.getAttribute("outputVariable");

			if (!outputVariable.isEmpty() && fromParts.hasAny()) {
				addViolation(invoke);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 52;
	}
}
