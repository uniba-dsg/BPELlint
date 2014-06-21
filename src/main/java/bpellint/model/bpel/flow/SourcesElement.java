package bpellint.model.bpel.flow;


import java.util.LinkedList;
import java.util.List;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

import nu.xom.Node;
import nu.xom.Nodes;

public class SourcesElement extends ContainerAwareReferable implements LinkEntityContainer {

	public SourcesElement(Node sources, ProcessContainer processContainer) {
		super(sources, processContainer);
		new NodeHelper(sources, "sources");
	}

	public List<SourceElement> getAllSources() {
		Nodes sourceElementNodes = toXOM().query("./bpel:source", Standards.CONTEXT);

		List<SourceElement> sourceElements = new LinkedList<>();
		for (Node source : sourceElementNodes) {
			sourceElements.add(new SourceElement(source, getProcessContainer()));
		}

		return sourceElements;
	}

	@Override
	public List<LinkEntity> getAll() {
		LinkedList<LinkEntity> linkEntities = new LinkedList<>();
		linkEntities.addAll(getAllSources());

		return linkEntities;
	}
}
