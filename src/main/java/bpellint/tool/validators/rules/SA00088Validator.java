package bpellint.tool.validators.rules;


import java.util.List;

import bpellint.model.NavigationException;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.CorrelationSetElement;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.model.wsdl.MessageElement;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00088Validator extends Validator {

	private static final int MISSING_CORRELATION_SET = 1;
	private static final int CORRELATION_SET_MISMATCH = 2;

	public SA00088Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			try {
				for (CorrelationElement correlation : onEvent.getCorrelations()) {
					try {
						CorrelationSetElement correlationSet = correlation
								.getCorrelationSet(onEvent.getAssociatedScope());
						checkMessageHasPropertyAlias(onEvent, correlationSet);
					} catch (NavigationException e) {
						addViolation(correlation, MISSING_CORRELATION_SET);
					}
				}
			} catch (NavigationException e) {
				// if no correlation exist, the rule cannot be applied
			}
		}

	}

	private void checkMessageHasPropertyAlias(OnEventElement onEvent,
			CorrelationSetElement correlationSet) {
		MessageElement message;
		try {
			message = onEvent.getOperation().getInput().getMessage();
		} catch (NavigationException e) {
			// ignore missing messages this is checked elsewhere
			return;
		}
		try {
			List<PropertyAliasElement> aliases = message.getPropertyAliases();
			for (PropertyAliasElement propertyAlias : aliases) {
				if (PrefixHelper.removePrefix(propertyAlias.getPropertyName()).equals(
						PrefixHelper.removePrefix(correlationSet.getPropertiesAttribute()))) {
					return;
				}
			}
		} catch (NavigationException e) {
			//see below
		}
		addViolation(onEvent, CORRELATION_SET_MISMATCH);
	}

	@Override
	public int getSaNumber() {
		return 88;
	}

}
