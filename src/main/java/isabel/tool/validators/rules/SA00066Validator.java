package isabel.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;
import nu.xom.ParentNode;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

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
		Nodes linkNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:link", Standards.CONTEXT);
		for (Node link : linkNodes) {
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

	private int count(String string, Node link) {
		int counter = 0;
		ParentNode flow = link.getParent().getParent();
		
		return 1;
	}

	@Override
	public int getSaNumber() {
		return 66;
	}

}
