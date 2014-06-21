package bpellint.model.bpel.var;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NavigationException;
import bpellint.model.Navigator;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.bpel.PartnerLinked;
import bpellint.model.bpel.PartnerLinkedImpl;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;
import nu.xom.Node;

public class CopyEntityImpl extends ContainerAwareReferable implements CopyEntity {

	private final NodeHelper fromTo;
	private final PartnerLinked partnerLinkDelegate;

	public CopyEntityImpl(Node node, ProcessContainer processContainer) {
		super(node, processContainer);
		this.fromTo = new NodeHelper(node);
		this.partnerLinkDelegate = new PartnerLinkedImpl(this, getProcessContainer(), getPartnerLinkAttribute());
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
	        if (fromTo.getAmountOfChildren() > 1) {
	            return false;
	        }
	        if (fromTo.getAmountOfChildren() == 1) {
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
        if (fromTo.getAmountOfChildren() > 0) {
            return false;
        }

        return fromTo.hasAttribute("partnerLink");
	}

	@Override
	public boolean isVariableAssignment() {
		if (!fromTo.hasAttribute("variable")) {
            return false;
        }
        if (fromTo.getAmountOfChildren() > 0) {
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
        if (fromTo.getAmountOfChildren() > 0) {
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

	@Override
	public PartnerLinkElement getPartnerLink() throws NavigationException {
		return partnerLinkDelegate.getPartnerLink();
	}

	private String getPartnerLinkAttribute() {
		return fromTo.getAttribute("partnerLink");
	}

	@Override
	public PropertyAliasElement getVariablePropertyAlias() throws NavigationException {
		String targetNamespace = PrefixHelper.resolveQNameToNamespace(fromTo.toXOM(), fromTo.getAttribute("property"));
		String name = PrefixHelper.removePrefix(fromTo.getAttribute("property"));
		Node property = getProcessContainer().resolveName(targetNamespace, name, "vprop:property");

		VariableLike variable = new Navigator(getProcessContainer()).getVariableByName(fromTo, fromTo.getAttribute("variable"));
		return variable.resolvePropertyAlias(new PropertyElement(property, getProcessContainer()));
	}

}
