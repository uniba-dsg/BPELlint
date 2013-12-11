package isabel.tool.validators.rules;

import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00051Validator extends Validator {

	public SA00051Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes invokes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);

		for (Node invoke : invokes) {

			Nodes toPartsSet = invoke.query("bpel:toParts", CONTEXT);
			Nodes inputVariableSet = invoke.query("@inputVariable", CONTEXT);
			if (toPartsSet.hasAny() && inputVariableSet.hasAny()) {
				addViolation(invoke);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 51;
	}

}
