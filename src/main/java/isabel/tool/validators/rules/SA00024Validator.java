package isabel.tool.validators.rules;

import isabel.tool.validators.result.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00024Validator extends Validator {

	public SA00024Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes variables = processContainer.getProcess().toXOM().query("//bpel:variable/@name", CONTEXT);
		checkForDots(variables);

		Nodes onEvents = processContainer.getProcess().toXOM().query("//bpel:onEvent/@variable", CONTEXT);
		checkForDots(onEvents);

		Nodes catches = processContainer.getProcess().toXOM().query("//bpel:catch/@faultVariable", CONTEXT);
		checkForDots(catches);
	}

	private void checkForDots(Nodes variableNames) {
		for (Node variableName : variableNames) {
			if (variableName.getValue().contains(".")) {
				addViolation(variableName);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 24;
	}
}
