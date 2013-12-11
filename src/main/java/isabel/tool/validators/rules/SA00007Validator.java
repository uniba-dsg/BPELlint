package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00007Validator extends Validator {

	public SA00007Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node compensateScope : getAllCompensateScopes()) {
			NodeHelper compensateScopeHelper = new NodeHelper(compensateScope);

			if (!compensateScopeHelper.hasAncestor("bpel:faultHandlers")
					&& !compensateScopeHelper
					.hasAncestor("bpel:compensationHandler")
					&& !compensateScopeHelper
					.hasAncestor("bpel:terminationHandlers")) {
				addViolation(compensateScope);
			}
		}
	}

	private Nodes getAllCompensateScopes() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:compensateScope", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 7;
	}

}
