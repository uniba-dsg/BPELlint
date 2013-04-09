package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.impl.ValidationCollector;

public class SA00025Validator extends Validator {

	private static final int MESSAGE_TYPE_AND_TYPE_AND_ELEMENT = 5;
	private static final int TYPE_AND_ELEMENT = 4;
	private static final int MESSAGE_TYPE_AND_ELEMENT = 3;
	private static final int MESSAGE_TYPE_AND_TYPE = 2;
	private static final int MESSAGE_TYPE_OR_TYPE_OR_ELEMENT_MISSING = 1;

	public SA00025Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes variables = fileHandler.getBpel().getDocument()
				.query("//bpel:variable", CONTEXT);

		for (Node variable : variables) {

			Nodes messageTypeSet = variable.query("@messageType", CONTEXT);
			Nodes typeSet = variable.query("@type", CONTEXT);
			Nodes elementSet = variable.query("@element", CONTEXT);

			if ((messageTypeSet.size() == 0) && (typeSet.size() == 0)
					&& (elementSet.size() == 0)) {
				addViolation(variable, MESSAGE_TYPE_OR_TYPE_OR_ELEMENT_MISSING);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0) {
				addViolation(variable, MESSAGE_TYPE_AND_TYPE);
			} else if (messageTypeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(variable, MESSAGE_TYPE_AND_ELEMENT);
			} else if (typeSet.size() > 0 && elementSet.size() > 0) {
				addViolation(variable, TYPE_AND_ELEMENT);
			} else if (messageTypeSet.size() > 0 && typeSet.size() > 0
					&& elementSet.size() > 0) {
				addViolation(variable, MESSAGE_TYPE_AND_TYPE_AND_ELEMENT);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 25;
	}
}
