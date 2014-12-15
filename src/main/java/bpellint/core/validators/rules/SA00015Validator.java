package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import nu.xom.Element;

public class SA00015Validator extends Validator {

	public SA00015Validator(ProcessContainer files,
	                        SimpleValidationResult violationCollector) {
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
