package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class TargetsElement extends ContainerAwareReferable implements LinkEntityContainer {

	public TargetsElement(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		new NodeHelper(node, "targets");
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
