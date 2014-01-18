package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.OnEventElement;
import isabel.tool.impl.ValidationCollector;

public class SA00089Validator extends Validator {

	public SA00089Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			if (!onEvent.getMessageExchangeAttribute().isEmpty()) {
				try {
					onEvent.getMessageExchange();
				} catch (NavigationException e) {
					addViolation(onEvent);
				}
			}
		}

	}

	@Override
	public int getSaNumber() {
		return 89;
	}

}
