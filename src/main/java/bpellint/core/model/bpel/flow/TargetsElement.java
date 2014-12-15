package bpellint.core.model.bpel.flow;


import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;

import nu.xom.Node;
import nu.xom.Nodes;

public class TargetsElement extends ContainerAwareReferable implements LinkEntityContainer {

	public TargetsElement(Node targets, ProcessContainer processContainer) {
		super(targets, processContainer);
		new NodeHelper(targets, "targets");
	}

	public List<TargetElement> getAllTargets() {
		Nodes targetElementNodes = toXOM().query("./bpel:target", Standards.CONTEXT);

		List<TargetElement> targetElements = new LinkedList<>();
		for (Node target : targetElementNodes) {
			targetElements.add(new TargetElement(target, getProcessContainer()));
		}

		return targetElements;
	}

	@Override
	public List<LinkEntity> getAll() {
		LinkedList<LinkEntity> linkEntities = new LinkedList<>();
		linkEntities.addAll(getAllTargets());

		return linkEntities;
	}
}
