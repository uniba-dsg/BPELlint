package isabel.tool.validators.rules;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.FlowElement;
import isabel.model.bpel.LinkedActivity;
import isabel.model.bpel.OptionalElementNotPresentException;
import isabel.model.bpel.SourceElement;
import isabel.model.bpel.TargetElement;
import isabel.tool.impl.ValidationCollector;

public class SA00072Validator extends Validator {

	private static final int CYCLIC = 1;
	private static final int NOT_VALIDLY_LINKED = 2;
	private Set<String> linkNames;

	public SA00072Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes sourceNodes = fileHandler.getBpel().getDocument().query("//bpel:source", Standards.CONTEXT);
		for (Node node : sourceNodes) {
			SourceElement source = new SourceElement(node);
				linkNames = new HashSet<>();
				linkNames.add(source.getLinkName());
				if (isCyclic(source, source.getLinkName())) {
					addViolation(node, CYCLIC);
					break;
				}
			
		}
	}

	private boolean isCyclic(SourceElement source, String linkName) {
		boolean cyclic = false;
		try {
			FlowElement flow = source.getLink().getFlow();
			TargetElement target = flow.getTargetElement(linkName);
			LinkedActivity activity = target.getActivity();
			try {
				for (SourceElement sourceElement : activity.getSourceElements()) {
					if (!linkNames.add(sourceElement.getLinkName())) {
						return true;
					}
					cyclic = cyclic
							|| isCyclic(sourceElement,
									sourceElement.getLinkName());
				}
			} catch (OptionalElementNotPresentException e) {
				// no source => reached an end of the graph => no cycles so far
				return false;
			}
		} catch (NavigationException e) {
			// ignore navigation error because a cycle can not exist without a target
		}
		
		return cyclic;
	}

	@Override
	public int getSaNumber() {
		return 72;
	}

}
