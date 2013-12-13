package isabel.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.bpel.FlowElement;
import isabel.model.bpel.LinkElement;

public class SA00066Validator extends Validator {

	private static final int TOO_MANY_SOURCES = 1;
	private static final int NO_SOURCE = 2;
	private static final int TOO_MANY_TARGETS = 3;
	private static final int NO_TARGET = 4;

	public SA00066Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (LinkElement link : fileHandler.getAllLinks()) {
			int amountOfSources = count("source", link);
			if (amountOfSources > 1) {
				addViolation(link, TOO_MANY_SOURCES);
			} else if (amountOfSources < 1) {
				addViolation(link, NO_SOURCE);
			}

			int amountOfTargets = count("target", link);
			if (amountOfTargets > 1) {
				addViolation(link, TOO_MANY_TARGETS);
			} else if (amountOfTargets < 1) {
				addViolation(link, NO_TARGET);
			}
		}

	}

	private int count(String linkEntity, LinkElement link) {
		int amountOfActivities = 0;
		String linkName = link.getName();
		FlowElement flow = link.getFlow();
		Nodes linkEntityContainers = flow.toXOM().query(".//bpel:" + linkEntity + "s",
				Standards.CONTEXT);
		for (Node linkEntityContainer : linkEntityContainers) {
			Nodes linkEntities = linkEntityContainer.query(".//bpel:"
					+ linkEntity + "[@linkName='" + linkName + "']",
					Standards.CONTEXT);
			if (linkEntities.hasAny()) {
				amountOfActivities++;
			}
		}

		return amountOfActivities;
	}

	@Override
	public int getSaNumber() {
		return 66;
	}

}
