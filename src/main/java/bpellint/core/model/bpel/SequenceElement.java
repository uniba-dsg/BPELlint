package bpellint.core.model.bpel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ComparableNode;
import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.NodesUtil;
import bpellint.core.model.ProcessContainer;

import nu.xom.Node;
import nu.xom.Nodes;

public class SequenceElement extends ContainerAwareReferable {

	public SequenceElement(Node sequence, ProcessContainer processContainer) {
		super(sequence, processContainer);
		new NodeHelper(sequence, "sequence");
	}

	public List<Node> getChildrenInOrder() {
		List<ComparableNode> children = new LinkedList<>();
		Nodes childNodes = toXOM().query("./*");
		for (Node node : childNodes) {
			children.add(new ComparableNode(node));
		}

		Collections.sort(children);

		return NodesUtil.toList(children);
	}

}
