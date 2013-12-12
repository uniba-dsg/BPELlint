package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.TargetElement;
import isabel.model.bpel.TargetsElement;
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
			checkLinkNameUniqueness(new TargetsElement(targets));
		}
	}

	private void checkLinkNameUniqueness(TargetsElement targets) {
		Set<String> uniqueNames = new HashSet<>();
		for (TargetElement target : targets.getAllTargets()) {
			String name = target.getLinkName();

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
