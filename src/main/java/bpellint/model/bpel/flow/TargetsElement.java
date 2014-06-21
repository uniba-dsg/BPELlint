package bpellint.model.bpel.flow;


import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

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
