package isabel.tool.validators.rules;

import isabel.tool.impl.ValidationCollector;
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
		Nodes variables = fileHandler.getBpel().getDocument()
				.query("//bpel:variable/@name", CONTEXT);
		Nodes onEvents = fileHandler.getBpel().getDocument()
				.query("//bpel:onEvent/@variable", CONTEXT);

		for (Node variable : variables) {
			if (variable.getValue().contains(".")) {
				addViolation(variable);
			}
		}

		for (Node onEvent : onEvents) {
			if (onEvent.getValue().contains(".")) {
				addViolation(onEvent);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 24;
	}
}
