package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;

import java.util.LinkedList;
import java.util.List;

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
