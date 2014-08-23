package bpellint.model.bpel.var;

import bpellint.model.*;
import bpellint.model.bpel.PartnerLinkElement;
import bpellint.model.bpel.PartnerLinked;
import bpellint.model.bpel.PartnerLinkedImpl;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;
import nu.xom.Node;

public class CopyEntityImpl extends ContainerAwareReferable implements CopyEntity {

    private final NodeHelper fromTo;
    private final PartnerLinked partnerLinkDelegate;
    private final int amountOfAttributes;
    private final int amountOfRealChildren;

    public CopyEntityImpl(Node node, ProcessContainer processContainer) {
        super(node, processContainer);
        this.fromTo = new NodeHelper(node);
        this.partnerLinkDelegate = new PartnerLinkedImpl(this, getProcessContainer(), getPartnerLinkAttribute());
        amountOfAttributes = fromTo.getAmountOfAttributes();
        if (fromTo.toXOM().query("./bpel:documentation", Standards.CONTEXT).hasAny()) {
            amountOfRealChildren = fromTo.getAmountOfChildren() - 1;
        } else {
            amountOfRealChildren = fromTo.getAmountOfChildren();
        }
    }

    @Override
    public boolean isEmpty() {
        return fromTo.hasNoChildren() && fromTo.hasNoAttributes() && !fromTo.hasContent();
    }

    @Override
    public boolean isMessageVariableAssignment() {
        if (fromTo.hasContent()){
            return false;
        }
        if (!fromTo.hasAttribute("variable")) {
            return false;
        }
        if (amountOfAttributes > 2 || amountOfRealChildren > 1) {
            return false;
        }
        if (amountOfRealChildren == 1) {
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

        return amountOfAttributes == 1 || fromTo.hasAttribute("part");
    }

    @Override
    public boolean isPartnerLinkAssignment() {
        if (fromTo.hasContent()){
            return false;
        }
        if ("from".equals(fromTo.getLocalName())) {
            if (amountOfAttributes != 2) {
                return false;
            }
            if (!"partnerRole".equals(fromTo.getAttribute("endpointReference"))
                    && !"myRole".equals(fromTo
                    .getAttribute("endpointReference"))) {
                return false;
            }
        } else {
            if (amountOfAttributes != 1) {
                return false;
            }
        }
        return amountOfRealChildren == 0 && fromTo.hasAttribute("partnerLink");
    }

    @Override
    public boolean isVariableAssignment() {
        if (fromTo.hasContent()){
            return false;
        }
        if (!fromTo.hasAttribute("variable")) {
            return false;
        }

        return fromTo.hasAttribute("property") && amountOfRealChildren == 0
                && amountOfAttributes == 2;
    }

    @Override
    public boolean isQueryResultAssignment() {
        if (amountOfAttributes > 1) {
            return false;
        }
        if (amountOfRealChildren > 0) {
            return false;
        }
        return amountOfAttributes == 0
                || fromTo.hasAttribute("expressionLanguage");
    }

    @Override
    public boolean isLiteralAssignment() {
        if (fromTo.hasContent()){
            return false;
        }
        if (!"from".equals(fromTo.getLocalName())) {
            return false;
        }
        if (!(amountOfAttributes == 0 && fromTo.asElement().getChildCount() > 0)) {
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
