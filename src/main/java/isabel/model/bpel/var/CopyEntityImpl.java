package isabel.model.bpel.var;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

public class CopyEntityImpl extends ContainerAwareReferable implements CopyEntity {

	private NodeHelper fromTo;

	public CopyEntityImpl(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		fromTo = new NodeHelper(node);
	}

	@Override
	public boolean isEmpty() {
		return fromTo.hasNoChildren() && fromTo.hasNoAttributes() && fromTo.hasNoContent();
	}

	@Override
	public boolean isMessageVariableAssignment() {
		 if (!fromTo.hasAttribute("variable")) {
	            return false;
	        }
	        if (fromTo.getAmountOfAttributes() > 2) {
	            return false;
	        }
	        if (fromTo.getAmountOfChildern() > 1) {
	            return false;
	        }
	        if (fromTo.getAmountOfChildern() == 1) {
	            NodeHelper query;
	            try {
	                query = fromTo.getFirstChildElement();
	                if (!"query".equals(query.getLocalName())) {
	                    return false;
	                }
	                if (query.getAmountOfAttributes() > 1) {
	                    return false;
	                }
	                if (!(query.hasAttribute("queryLanguage") || query
	                        .getAmountOfAttributes() == 0)) {
	                    return false;
	                }
	            } catch (NavigationException e) {
	                return false;
	            }
	        }

	        return fromTo.getAmountOfAttributes() == 1
	                || fromTo.hasAttribute("part");
	}

	@Override
	public boolean isPartnerLinkAssignment() {
		if ("from".equals(fromTo.getLocalName())) {
            if (fromTo.getAmountOfAttributes() != 2) {
                return false;
            }
            if (!"partnerRole".equals(fromTo.getAttribute("endpointReference"))
                    && !"myRole".equals(fromTo
                    .getAttribute("endpointReference"))) {
                return false;
            }
        } else {
            if (fromTo.getAmountOfAttributes() != 1) {
                return false;
            }
        }
        if (fromTo.getAmountOfChildern() > 0) {
            return false;
        }

        return fromTo.hasAttribute("partnerLink");
	}

	@Override
	public boolean isVariableAssignment() {
		if (!fromTo.hasAttribute("variable")) {
            return false;
        }
        if (fromTo.getAmountOfChildern() > 0) {
            return false;
        }

        return fromTo.hasAttribute("property")
                && fromTo.getAmountOfAttributes() == 2;
	}

	@Override
	public boolean isQueryResultAssignment() {
		if (fromTo.getAmountOfAttributes() > 1) {
            return false;
        }
        if (fromTo.getAmountOfChildern() > 0) {
            return false;
        }
        return fromTo.getAmountOfAttributes() == 0
                || fromTo.hasAttribute("expressionLanguage");
	}

	@Override
	public boolean isLiteralAssignment() {
		if (!"from".equals(fromTo.getLocalName())) {
            return false;
        }
        if (!(fromTo.getAmountOfAttributes() == 0 && fromTo.asElement().getChildCount() > 0)) {
            return false;
        }

        try {
            NodeHelper literal = fromTo.getFirstChildElement();
            return "literal".equals(literal.getLocalName());
        } catch (NavigationException e) {
            return false;
        }
	}

}
