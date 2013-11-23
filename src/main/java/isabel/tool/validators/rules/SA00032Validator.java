package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;
import isabel.tool.ValidationException;
import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.NodesUtil;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00032Validator extends Validator {

	public SA00032Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<Node> fromTos = getFroms();
		fromTos.addAll(getTos());

		equalsConformantVariant(fromTos);
	}

	private List<Node> getTos() {
		Nodes toNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:to", CONTEXT);
		return NodesUtil.toList(toNodes);
	}

	private List<Node> getFroms() {
		Nodes fromNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:copy/bpel:from", CONTEXT);
		return NodesUtil.toList(fromNodes);
	}

	private void equalsConformantVariant(List<Node> fromTos) {
		for (Node fromTo : fromTos) {
			if (!(isEmpty(fromTo) || isMessageVariableAssignment(fromTo)
					|| isPartnerLinkAssignment(fromTo)
					|| isVariableAssignment(fromTo)
					|| isQueryResultAssignment(fromTo) || isLiteralAssignment(fromTo))) {
				addViolation(fromTo);
			}
		}
	}

	private boolean isEmpty(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

		boolean noChildren = fromTo.getAmountOfChildern() == 0;
		boolean noAttributes = fromTo.hasNoAttributes();
		boolean noContent = fromToNode.getValue().trim().isEmpty();

		return noChildren && noAttributes && noContent;
	}

	private boolean isMessageVariableAssignment(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

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
			} catch (ValidationException e) {
				return false;
			}
		}

		return fromTo.getAmountOfAttributes() == 1
				|| fromTo.hasAttribute("part");
	}

	private boolean isPartnerLinkAssignment(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

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

	private boolean isVariableAssignment(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

		if (!fromTo.hasAttribute("variable")) {
			return false;
		}
		if (fromTo.getAmountOfChildern() > 0) {
			return false;
		}

		return fromTo.hasAttribute("property")
				&& fromTo.getAmountOfAttributes() == 2;
	}

	private boolean isQueryResultAssignment(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

		if (fromTo.getAmountOfAttributes() > 1) {
			return false;
		}
		if (fromTo.getAmountOfChildern() > 0) {
			return false;
		}
		return fromTo.getAmountOfAttributes() == 0
				|| fromTo.hasAttribute("expressionLanguage");
	}

	private boolean isLiteralAssignment(Node fromToNode) {
		NodeHelper fromTo = new NodeHelper(fromToNode);

		if (!"from".equals(fromTo.getLocalName())) {
			return false;
		}
		if (!(fromTo.getAmountOfAttributes() == 0 && fromToNode.getChildCount() > 0)) {
			return false;
		}

		try {
			NodeHelper literal = fromTo.getFirstChildElement();
			return "literal".equals(literal.getLocalName());
		} catch (ValidationException e) {
			return false;
		}
	}

	@Override
	public int getSaNumber() {
		return 32;
	}

}
