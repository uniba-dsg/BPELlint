package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.bpel.mex.OnEventElement;

public class SA00084Validator extends Validator {

	private static final int OPERATION_UNDEFINED = 2;
	private static final int MY_ROLE_REQUIRED = 1;
	private static final int MISSING_PARTNER_LINK = 3;

	public SA00084Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (OnEventElement onEvent : processContainer.getAllOnEvents()) {
			try {
				PartnerLinkElement partnerLink = onEvent.getPartnerLink();
				if (!partnerLink.hasMyRole()) {
					addViolation(partnerLink, MY_ROLE_REQUIRED);
				}
				try {
					onEvent.getOperation();
				} catch (NavigationException e) {
					addViolation(partnerLink, OPERATION_UNDEFINED);
				}
			} catch (NavigationException e) {
				addViolation(onEvent, MISSING_PARTNER_LINK);
			}
		}

	}

	@Override
	public int getSaNumber() {
		return 84;
	}

}
