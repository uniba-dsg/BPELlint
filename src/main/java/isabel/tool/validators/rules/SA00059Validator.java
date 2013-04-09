package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;

public class SA00059Validator extends Validator {

	public SA00059Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes replies = fileHandler.getBpel().getDocument()
				.query("//bpel:reply", CONTEXT);

		for (Node reply : replies) {
			NodeHelper nodeHelper = new NodeHelper(reply);

			if (nodeHelper.hasQueryResult("bpel:toParts")
					&& nodeHelper.hasAttribute("variable")) {
				addViolation(reply);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}