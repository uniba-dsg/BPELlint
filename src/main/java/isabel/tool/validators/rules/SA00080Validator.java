package isabel.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

public class SA00080Validator extends Validator {

	public SA00080Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes faultHandlers = fileHandler.getBpel().getDocument()
				.query("//bpel:faultHandlers", Standards.CONTEXT);
		for (Node faultHandler : faultHandlers) {
			NodeHelper faultHandlerHelper = new NodeHelper(faultHandler);

			if (faultHandlerHelper.hasEmptyQueryResult("bpel:catch")
					&& faultHandlerHelper.hasEmptyQueryResult("bpel:catchAll")) {
				addViolation(faultHandler);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 80;
	}
}
