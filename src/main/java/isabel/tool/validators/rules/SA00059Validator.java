package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.bpel.ReplyElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00059Validator extends Validator {

	public SA00059Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		for (ReplyElement reply : fileHandler.getAllReplies()) {
			if (reply.hasQueryResult("bpel:toParts")
					&& reply.hasAttribute("variable")) {
				addViolation(reply);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}