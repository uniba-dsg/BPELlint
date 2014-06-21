package bpellint.tool.validators.rules;

import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.tool.validators.result.ValidationCollector;
import nu.xom.Element;

public class SA00015Validator extends Validator {

	public SA00015Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Element rootElement = this.processContainer.getBpel().getDocument()
				.getRootElement();
		NodeHelper processElement = new NodeHelper(rootElement);
		boolean hasReceiveWithCreateInstanceYes = processElement
				.hasQueryResult("//bpel:receive[@createInstance='yes']");
		boolean hasPickWithCreateInstanceYes = processElement
				.hasQueryResult("//bpel:pick[@createInstance='yes']");
		if (!hasReceiveWithCreateInstanceYes && !hasPickWithCreateInstanceYes) {
			this.addViolation(rootElement);
		}
	}

	@Override
	public int getSaNumber() {
		return 15;
	}
}
