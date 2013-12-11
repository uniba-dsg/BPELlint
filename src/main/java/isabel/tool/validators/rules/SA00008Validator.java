package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00008Validator extends Validator {

	public SA00008Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node compensateScope : getAllCompensates()) {
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

	private Nodes getAllCompensates() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:compensate", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 8;
	}

}
