package isabel.model.bpel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ComparableNode;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.NodesUtil;
import isabel.model.ProcessContainer;

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
