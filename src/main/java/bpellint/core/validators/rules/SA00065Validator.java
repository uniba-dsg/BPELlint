package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.flow.LinkEntity;

public class SA00065Validator extends Validator {

	public SA00065Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (LinkEntity linkEntity : processContainer.getAllLinkEntities()) {
			try {
				linkEntity.getLink();
			} catch (NavigationException e) {
				addViolation(linkEntity);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 65;
	}

}
