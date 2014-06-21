package bpellint.tool.validators.rules;

import java.util.HashSet;
import java.util.Set;

import bpellint.model.ProcessContainer;
import bpellint.model.bpel.flow.LinkElement;
import bpellint.model.bpel.flow.LinksElement;
import bpellint.tool.validators.result.ValidationCollector;


public class SA00064Validator extends Validator {

	public SA00064Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
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
