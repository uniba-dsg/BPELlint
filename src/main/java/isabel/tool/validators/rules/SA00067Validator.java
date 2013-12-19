package isabel.tool.validators.rules;

import isabel.model.ComparableNode;
import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.LinkElement;
import isabel.model.bpel.LinkEntity;
import isabel.model.bpel.SourcesElement;
import isabel.model.bpel.TargetsElement;
import isabel.tool.impl.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

public class SA00067Validator extends Validator {

	public SA00067Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (SourcesElement sources : fileHandler.getAllSourcesContainer()) {
			for (TargetsElement targetsNode : fileHandler.getAllTargetsContainer()) {
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
				if (areEqual(source.getLink(), target.getLink())) {
					linkNames.add(linkName);
				}
			} catch (NavigationException e) {
				// ignore because a connection is not possible is a link is missing
			}
		}
		return linkNames;
	}
	
	private boolean areEqual(LinkElement linkA, LinkElement linkB) {
		return new ComparableNode(linkA.toXOM()).equals(new ComparableNode(linkB.toXOM()));
	}

	@Override
	public int getSaNumber() {
		return 67;
	}

}
