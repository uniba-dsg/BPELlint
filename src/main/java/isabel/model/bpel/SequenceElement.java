package isabel.model.bpel;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ComparableNode;
import isabel.model.NodeHelper;
import isabel.model.NodesUtil;
import isabel.model.Referable;

public class SequenceElement implements Referable {

	private NodeHelper sequence;

	public SequenceElement(Node node) {
		sequence = new NodeHelper(node, "sequence");
	}
	
	public List<Node> getChildsInOrder() {
		List<ComparableNode> childs = new LinkedList<>();
		Nodes childNodes = sequence.toXOM().query("./*");
		for (Node node : childNodes) {
			childs.add(new ComparableNode(node));
		}
		
		Collections.sort(childs);
		
		return NodesUtil.toList(childs);
	}

	@Override
	public Node toXOM() {
		return sequence.toXOM();
	}

}
