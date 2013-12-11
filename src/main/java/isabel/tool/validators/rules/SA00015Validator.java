package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Element;

public class SA00015Validator extends Validator {

	public SA00015Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Element rootElement = this.fileHandler.getBpel().getDocument()
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
