package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.mex.OnEventElement;

public class SA00089Validator extends Validator {

	public SA00089Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
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
