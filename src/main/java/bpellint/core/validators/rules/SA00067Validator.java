package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.flow.LinkEntity;
import bpellint.core.model.bpel.flow.SourcesElement;
import bpellint.core.model.bpel.flow.TargetsElement;

public class SA00067Validator extends Validator {

	public SA00067Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (SourcesElement sources : processContainer.getAllSourcesContainer()) {
			for (TargetsElement targetsNode : processContainer.getAllTargetsContainer()) {
				checkForDoubleLinks(sources, targetsNode);
			}
		}
	}

	private void checkForDoubleLinks(SourcesElement sources, TargetsElement targets) {
			if (!haveMaxOneSharedLink(targets, sources)) {
				addViolation(sources);
				addViolation(targets);
			}
	}

	private boolean haveMaxOneSharedLink(TargetsElement targets, SourcesElement sources) {
		Set<String> linkNames = new HashSet<>();
		for (LinkEntity source : sources.getAll()) {
			for (LinkEntity target : targets.getAll()) {
				linkNames.addAll(collectEqualLinkNames(source, target));
			}
		}
		return linkNames.size() < 2;
	}

	private Set<String> collectEqualLinkNames(LinkEntity source, LinkEntity target) {
		Set<String> linkNames = new HashSet<>();
		String linkName = target.getLinkName();
		if (source.getLinkName().equals(linkName)) {
			try {
				if (source.getLink().equals(target.getLink())) {
					linkNames.add(linkName);
				}
			} catch (NavigationException e) {
				// ignore because a connection is not possible if a link is missing
			}
		}
		return linkNames;
	}

	@Override
	public int getSaNumber() {
		return 67;
	}

}
