package bpellint.core.validators.rules;

import api.SimpleValidationResult;
import bpellint.core.model.ComparableNode;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Standards;
import bpellint.core.model.bpel.flow.LinkElement;
import bpellint.core.model.bpel.flow.LinkEntity;
import bpellint.core.model.bpel.flow.SourceElement;
import nu.xom.Nodes;

public class SA00070Validator extends Validator {

	public SA00070Validator(ProcessContainer files, SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (LinkEntity sourceOrTarget : processContainer.getAllLinkEntities()) {
			checkForBoundaryElement(sourceOrTarget);
		}
	}

	private void checkForBoundaryElement(LinkEntity sourceOrTarget)  {
		NodeHelper node = new NodeHelper(sourceOrTarget.toXOM());
		while (!"process".equals(node.getParent().getLocalName())) {
			node = node.getParent();
			if (isBoundaryElement(node.getLocalName())) {
				checkGraphInBoundary(sourceOrTarget, node);
				break;
			}
		}
	}

	private void checkGraphInBoundary(LinkEntity sourceOrTarget, NodeHelper node) {
		try {
			if (!hasPartnerInScope(sourceOrTarget, node) || !isEnclosed(sourceOrTarget.getLink(), node)) {
				addViolation(sourceOrTarget);
			}
		} catch (NavigationException e) {
			// ignore missing link definition. This is checked elsewhere.
		}
	}

	private boolean isBoundaryElement(String element) {
		return "while".equals(element) || "compensationHandler".equals(element)
				|| "eventHandlers".equals(element) || "repeatUntil".equals(element)
				|| "forEach".equals(element);
	}

	private boolean hasPartnerInScope(LinkEntity sourceOrTarget, NodeHelper boundary) {
		if (sourceOrTarget instanceof SourceElement) {
			return hasPartnerInScope("target", sourceOrTarget.getLinkName(), boundary);
		} else {
			return hasPartnerInScope("source", sourceOrTarget.getLinkName(), boundary);
		}
	}

	private boolean hasPartnerInScope(String sourceOrTarget, String linkName, NodeHelper boundary) {
		Nodes partner = boundary.toXOM().query(".//bpel:" + sourceOrTarget + "[@linkName='" + linkName + "']",
				Standards.CONTEXT);
		return partner.hasAny();
	}

	private boolean isEnclosed(LinkElement link, NodeHelper boundary) {
		NodeHelper node = new NodeHelper(link.toXOM());
		while (!"process".equals(node.getParent().getLocalName())) {
			node = node.getParent();
			if (new ComparableNode(node).equals(new ComparableNode(boundary))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSaNumber() {
		return 70;
	}

}
