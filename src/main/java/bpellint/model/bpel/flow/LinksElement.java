package bpellint.model.bpel.flow;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

import nu.xom.Node;
import nu.xom.Nodes;

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
