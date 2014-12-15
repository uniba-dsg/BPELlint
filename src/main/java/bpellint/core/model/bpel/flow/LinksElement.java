package bpellint.core.model.bpel.flow;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;

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
