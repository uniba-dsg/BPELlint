package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

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
