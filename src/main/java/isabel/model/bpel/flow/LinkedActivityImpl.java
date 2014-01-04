package isabel.model.bpel.flow;

import isabel.model.ContainerAwareReferable;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.OptionalElementNotPresentException;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class LinkedActivityImpl extends ContainerAwareReferable implements LinkedActivity{

	public LinkedActivityImpl(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
	}

	public LinkedActivityImpl(NodeHelper nodeHelper, ProcessContainer processContainer) {
		this(nodeHelper.toXOM(), processContainer);
	}

	@Override
	public List<SourceElement> getSourceElements()
			throws OptionalElementNotPresentException {
		Nodes sources = toXOM().query("./bpel:sources",Standards.CONTEXT);
		if(!sources.hasAny()){
			throw new OptionalElementNotPresentException("Invoke is not the source of any link");
		}

		return new SourcesElement(sources.get(0), getProcessContainer()).getAllSources();
	}

	@Override
	public List<TargetElement> getTargetElements()
			throws OptionalElementNotPresentException {
		Nodes targets = toXOM().query("./bpel:targets",Standards.CONTEXT);
		if(!targets.hasAny()){
			throw new OptionalElementNotPresentException("Invoke is not the source of any link");
		}

		return new TargetsElement(targets.get(0), getProcessContainer()).getAllTargets();
	}

}
