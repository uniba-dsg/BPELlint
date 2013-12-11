package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00069Validator extends Validator {

	public SA00069Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes targetsNodes = this.fileHandler.getBpel().getDocument()
				.query("//bpel:targets", Standards.CONTEXT);
		for (Node targets : targetsNodes) {
			checkLinkNameUniqueness(targets);
		}
	}

	private void checkLinkNameUniqueness(Node targets) {
		Set<String> uniqueNames = new HashSet<>();
		for (Node target : targets.query("./bpel:target", Standards.CONTEXT)) {
			String name = new NodeHelper(target).getAttribute("linkName");

			if (uniqueNames.contains(name)) {
				addViolation(target);
			} else {
				uniqueNames.add(name);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 69;
	}

}
