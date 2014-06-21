package bpellint.model.bpel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bpellint.model.ComparableNode;
import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.NodesUtil;
import bpellint.model.ProcessContainer;

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
