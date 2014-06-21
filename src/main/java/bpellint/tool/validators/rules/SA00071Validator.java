package bpellint.tool.validators.rules;

import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import bpellint.model.bpel.flow.TargetElement;
import bpellint.tool.validators.result.ValidationCollector;
import nu.xom.Nodes;

public class SA00071Validator extends Validator {

	public SA00071Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (TargetElement target : processContainer.getAllTargets()) {
			checkIsInFaultOrTerminationHandler(target);
		}
	}

	private void checkIsInFaultOrTerminationHandler(TargetElement target) {
		NodeHelper node = new NodeHelper(target.toXOM());
		while (!"process".equals(node.getParent().getLocalName())) {
			node = node.getParent();
			if (isFaultOrTerminationHandler(node)) {
				Nodes source = correspondingSource(target, node);
				if (!source.hasAny()) {
					addViolation(target);
					break;
				}
			}
		}
	}
	
	private boolean isFaultOrTerminationHandler(NodeHelper node) {
		return "catch".equals(node.getLocalName())
				|| "catchAll".equals(node.getLocalName())
				|| "terminationHandler".equals(node.getLocalName());
	}

	private Nodes correspondingSource(TargetElement target, NodeHelper node) {
		return node.toXOM().query(
				".//bpel:source[@linkName='" + target.getLinkName()
						+ "']", Standards.CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 71;
	}

}
