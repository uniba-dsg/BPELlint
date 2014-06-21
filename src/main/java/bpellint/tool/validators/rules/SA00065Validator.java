package bpellint.tool.validators.rules;

import bpellint.model.NavigationException;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.flow.LinkEntity;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00065Validator extends Validator {

	public SA00065Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
