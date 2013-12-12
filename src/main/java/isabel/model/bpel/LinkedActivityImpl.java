package isabel.model.bpel;

import isabel.model.NodeHelper;
import isabel.model.Standards;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class LinkedActivityImpl implements LinkedActivity{

	private NodeHelper nodeHelper;

	public LinkedActivityImpl(Node node) {
		this.nodeHelper = new NodeHelper(node);
	}
	
	public LinkedActivityImpl(NodeHelper nodeHelper) {
		this(nodeHelper.toXOM());
	}

	@Override
	public List<SourceElement> getSourceElements()
			throws OptionalElementNotPresentException {
		Nodes sources = nodeHelper.toXOM().query("./bpel:sources",Standards.CONTEXT);
		if(!sources.hasAny()){
			throw new OptionalElementNotPresentException("Invoke is not the source of any link");
		}
		
		return new SourcesElement(sources.get(0)).getAllSources();
	}

	@Override
	public List<TargetElement> getTargetElements()
			throws OptionalElementNotPresentException {
		Nodes targets = nodeHelper.toXOM().query("./bpel:targets",Standards.CONTEXT);
		if(!targets.hasAny()){
			throw new OptionalElementNotPresentException("Invoke is not the source of any link");
		}
		
		return new TargetsElement(targets.get(0)).getAllTargets();
	}

}
