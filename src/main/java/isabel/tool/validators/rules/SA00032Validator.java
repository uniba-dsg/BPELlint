package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.tool.impl.ValidationCollector;

import java.util.LinkedList;
import java.util.List;

public class SA00032Validator extends Validator {

    public SA00032Validator(ProcessContainer files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        List<NodeHelper> fromAndToElements = new LinkedList<>();
        fromAndToElements.addAll(fileHandler.getAllTos());
        fromAndToElements.addAll(fileHandler.getAllFroms());

        equalsConformantVariant(fromAndToElements);
    }

    private void equalsConformantVariant(List<NodeHelper> fromTos) {
        for (NodeHelper fromTo : fromTos) {
            if (!(isEmpty(fromTo) || isMessageVariableAssignment(fromTo)
                    || isPartnerLinkAssignment(fromTo)
                    || isVariableAssignment(fromTo)
                    || isQueryResultAssignment(fromTo) || isLiteralAssignment(fromTo))) {
                addViolation(fromTo);
            }
        }
    }

    private boolean isEmpty(NodeHelper fromTo) {
        return fromTo.hasNoChildren() && fromTo.hasNoAttributes() && fromTo.hasNoContent();
    }

    private boolean isMessageVariableAssignment(NodeHelper fromTo) {
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

    private boolean isPartnerLinkAssignment(NodeHelper fromTo) {
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

    private boolean isVariableAssignment(NodeHelper fromTo) {
        if (!fromTo.hasAttribute("variable")) {
            return false;
        }
        if (fromTo.getAmountOfChildern() > 0) {
            return false;
        }

        return fromTo.hasAttribute("property")
                && fromTo.getAmountOfAttributes() == 2;
    }

    private boolean isQueryResultAssignment(NodeHelper fromTo) {
        if (fromTo.getAmountOfAttributes() > 1) {
            return false;
        }
        if (fromTo.getAmountOfChildern() > 0) {
            return false;
        }
        return fromTo.getAmountOfAttributes() == 0
                || fromTo.hasAttribute("expressionLanguage");
    }

    private boolean isLiteralAssignment(NodeHelper fromTo) {
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
    public int getSaNumber() {
        return 32;
    }

}
