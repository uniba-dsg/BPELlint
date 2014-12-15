package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.ReplyElement;

public class SA00059Validator extends Validator {

	public SA00059Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
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