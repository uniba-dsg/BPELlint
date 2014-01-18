package isabel.tool.validators.rules;

import isabel.model.bpel.mex.ReplyElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;

public class SA00059Validator extends Validator {

	public SA00059Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (ReplyElement reply : processContainer.getAllReplies()) {
			if (new NodeHelper(reply).hasQueryResult("bpel:toParts")
					&& new NodeHelper(reply).hasAttribute("variable")) {
				addViolation(reply);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 59;
	}
}