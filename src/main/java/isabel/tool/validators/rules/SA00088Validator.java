package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.CorrelationSetElement;
import isabel.model.bpel.mex.OnEventElement;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.tool.impl.ValidationCollector;

import java.util.List;

public class SA00088Validator extends Validator {

	private static final int MISSING_CORRELATION_SET = 1;
	private static final int CORRELATION_SET_MISSMATCH = 2;

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
								.getCorrelationSet(onEvent.getAsociatedScope());
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
		addViolation(onEvent, CORRELATION_SET_MISSMATCH);
	}

	@Override
	public int getSaNumber() {
		return 88;
	}

}
