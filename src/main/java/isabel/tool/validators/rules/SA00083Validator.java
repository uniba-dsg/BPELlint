package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00083Validator extends Validator {

	public SA00083Validator(ProcessContainer files,
	                        ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes faultHandlers = fileHandler.getBpel().getDocument()
				.query("//bpel:eventHandlers", Standards.CONTEXT);
		for (Node faultHandler : faultHandlers) {
			NodeHelper faultHandlerHelper = new NodeHelper(faultHandler);

			if (faultHandlerHelper.hasEmptyQueryResult("bpel:onEvent")
					&& faultHandlerHelper.hasEmptyQueryResult("bpel:onAlarm")) {
				addViolation(faultHandler);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 83;
	}
}
