package bpellint.core.validators.rules;

import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.flow.LinkElement;
import bpellint.core.model.bpel.flow.LinksElement;


public class SA00064Validator extends Validator {

	public SA00064Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (LinksElement links : processContainer.getAllLinksContainer()) {
			checkLinksNameUniqueness(links);
		}
	}

	private void checkLinksNameUniqueness(LinksElement linksElement) {
		Set<String> uniqueNames = new HashSet<>();
		for (LinkElement link : linksElement.getAllLinks()) {
			String name = link.getName();
			
			if (uniqueNames.contains(name)) {
				addViolation(link);
			} else {
				uniqueNames.add(name);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 64;
	}

}
