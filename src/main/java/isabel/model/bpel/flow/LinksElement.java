package isabel.model.bpel.flow;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

public class LinksElement extends ContainerAwareReferable {

	public LinksElement(Node links, ProcessContainer processContainer) {
		super(links, processContainer);
		new NodeHelper(links, "links");
	}

	public List<LinkElement> getAllLinks() {
		Nodes linkElementNodes = toXOM().query("./bpel:link", Standards.CONTEXT);

		List<LinkElement> linkElements = new LinkedList<>();
		for (Node link : linkElementNodes) {
			linkElements.add(new LinkElement(link, getProcessContainer()));
		}

		return linkElements;
	}

}
