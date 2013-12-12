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
		for (ReceiveElement receive : fileHandler.getAllReceives()) {
			if (receive.hasFromParts() && receive.hasVariable()) {
				addViolation(receive);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 55;
	}
}