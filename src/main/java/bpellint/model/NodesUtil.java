package bpellint.model;

import nu.xom.Node;
import nu.xom.Nodes;

import java.util.ArrayList;
import java.util.List;

public class NodesUtil {

	public static List<Node> toList(Nodes nodes) {
		List<Node> nodeList = new ArrayList<>(nodes.size());

		for (Node node : nodes) {
			nodeList.add(node);
		}

		return nodeList;
	}

    public static <T extends Referable> List<Node> toList(List<T> nodes) {
        List<Node> nodeList = new ArrayList<>(nodes.size());

        for (T node : nodes) {
            nodeList.add(node.toXOM());
        }

        return nodeList;
    }

}
