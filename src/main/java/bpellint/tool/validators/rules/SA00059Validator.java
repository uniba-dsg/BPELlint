package bpellint.tool.validators.rules;

import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.ReplyElement;
import bpellint.tool.validators.result.ValidationCollector;

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