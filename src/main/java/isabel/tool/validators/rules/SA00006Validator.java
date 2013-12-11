package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00006Validator extends Validator {

	public SA00006Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node rethrow : getAllRethrows()) {
			NodeHelper rethrowHelper = new NodeHelper(rethrow);

			if (!rethrowHelper.hasAncestor("bpel:faultHandlers")) {
				addViolation(rethrow);
			}
		}
	}

	private Nodes getAllRethrows() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:rethrow", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 6;
	}

}
