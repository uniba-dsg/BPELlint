package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.flow.LinkEntity;
import isabel.tool.validators.result.ValidationCollector;

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
