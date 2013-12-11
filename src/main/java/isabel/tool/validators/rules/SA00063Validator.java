package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00063Validator extends Validator {

	public SA00063Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes onMessages = fileHandler.getBpel().getDocument()
				.query("//bpel:onMessage", CONTEXT);

		for (Node onMessage : onMessages) {
			NodeHelper nodeHelper = new NodeHelper(onMessage);
			if (nodeHelper.hasQueryResult("bpel:fromParts")
					&& nodeHelper.hasAttribute("variable")) {
				addViolation(onMessage);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 63;
	}
}
