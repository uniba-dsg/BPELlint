package isabel.tool.validators.rules;

import isabel.model.bpel.ReceiveElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00055Validator extends Validator {

	public SA00055Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		Nodes receives = fileHandler.getBpel().getDocument()
				.query("//bpel:receive", CONTEXT);

		for (Node receive : receives) {
			ReceiveElement receiveHelper = new ReceiveElement(receive);

			if (receiveHelper.hasFromParts() && receiveHelper.hasVariable()) {
				addViolation(receive);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}