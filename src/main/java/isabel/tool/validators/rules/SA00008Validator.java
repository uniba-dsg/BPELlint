package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;

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
