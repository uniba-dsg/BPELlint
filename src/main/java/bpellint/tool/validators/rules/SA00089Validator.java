package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.tool.validators.result.ValidationCollector;

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
