package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.PartnerLinkElement;
import isabel.model.bpel.mex.OnEventElement;
import isabel.tool.impl.ValidationCollector;

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
		for (OnEventElement onEvent : fileHandler.getAllOnEvents()) {
			try {
				PartnerLinkElement partnerLink = onEvent.getPartnerLink();
				if (!partnerLink.hasMyRole()) {
					addViolation(partnerLink, MY_ROLE_REQUIRED);
				}
				try {
					onEvent.getOperation();
				} catch (NavigationException e) {
					e.printStackTrace();
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
