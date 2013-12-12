package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class TargetsElement implements Referable, LinkEntityContainer {

	private final NodeHelper targets;
	
	public TargetsElement(Node node) {
		targets = new NodeHelper(node, "targets");
	}
	
	public List<TargetElement> getAllTargets() {
		Nodes targetElementNodes = targets.toXOM().query("./bpel:target",Standards.CONTEXT);
		
		List<TargetElement> targetElements = new LinkedList<>();
		for (Node target : targetElementNodes) {
			targetElements.add(new TargetElement(target));
		}
		
		return targetElements;
	}
	
	@Override
	public Node toXOM() {
		return targets.toXOM();
	}

	@Override
	public List<LinkEntity> getAll() {
		LinkedList<LinkEntity> linkEntities = new LinkedList<>();
		for (LinkEntity target : getAllTargets()) {
			linkEntities.add(target);
		}
		return linkEntities;
	}
}
