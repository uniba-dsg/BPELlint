package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.impl.ValidationCollector;

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
			if ((toPartsSet.size() > 0) && (inputVariableSet.size() > 0)) {
				addViolation(invoke);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 51;
	}

}
