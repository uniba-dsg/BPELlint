package isabel.model.bpel;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

public class LinksElement implements Referable {

	private final NodeHelper links;
	
	public LinksElement(Node node) {
		links = new NodeHelper(node, "links");
	}
	
	public List<LinkElement> getAllLinks() {
		Nodes linkElementNodes = links.toXOM().query("./bpel:link",Standards.CONTEXT);
		
		List<LinkElement> linkElements = new LinkedList<>();
		for (Node link : linkElementNodes) {
			linkElements.add(new LinkElement(link));
		}
		
		return linkElements;
	}
	
	@Override
	public Node toXOM() {
		return links.toXOM();
	}

}
