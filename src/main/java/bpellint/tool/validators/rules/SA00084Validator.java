package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.bpel.mex.OnEventElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00084Validator extends Validator {

	private static final int OPERATION_UNDEFINED = 2;
	private static final int MY_ROLE_REQUIRED = 1;
	private static final int MISSING_PARTNER_LINK = 3;

	public SA00084Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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