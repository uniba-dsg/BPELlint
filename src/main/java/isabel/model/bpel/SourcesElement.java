package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Referable;
import isabel.model.Standards;

import java.util.LinkedList;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class SourcesElement implements Referable, LinkEntityContainer {

	private final NodeHelper sources;
	
	public SourcesElement(Node node) {
		sources = new NodeHelper(node, "sources");
	}
	
	public List<SourceElement> getAllSources() {
		Nodes sourceElementNodes = sources.toXOM().query("./bpel:source",Standards.CONTEXT);
		
		List<SourceElement> sourceElements = new LinkedList<>();
		for (Node source : sourceElementNodes) {
			sourceElements.add(new SourceElement(source));
		}
		
		return sourceElements;
	}
	
	@Override
	public Node toXOM() {
		return sources.toXOM();
	}

	@Override
	public List<LinkEntity> getAll() {
		LinkedList<LinkEntity> linkEntities = new LinkedList<>();
		for (LinkEntity target : getAllSources()) {
			linkEntities.add(target);
		}
		return linkEntities;
	}
}
