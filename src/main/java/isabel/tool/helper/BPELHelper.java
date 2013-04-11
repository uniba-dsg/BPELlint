package isabel.tool.helper;

import isabel.tool.impl.NavigationException;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;

public class BPELHelper {

	public static Node getVariableByName(Node startNode, String variableName)
			throws NavigationException {
		if ("onEvent".equals(NodeHelper.toElement(startNode).getLocalName())) {
			NodeHelper nodeHelper = new NodeHelper(startNode);
			String variableAttribute = nodeHelper.getAttribute("variable");
			if (variableAttribute.equals(variableName))
				if (nodeHelper.hasAttribute("messageType")
						|| nodeHelper.hasAttribute("element"))
					return startNode;
		} else if ("scope".equals(NodeHelper.toElement(startNode)
				.getLocalName())) {
			Nodes scopeVariableSet = startNode.query(
					"child::bpel:variables/bpel:variable", CONTEXT);
			for (Node variable : scopeVariableSet) {
				String name = new NodeHelper(variable).getAttribute("name");
				if (name.equals(variableName))
					return variable;
			}
		} else if ("process".equals(NodeHelper.toElement(startNode)
				.getLocalName())) {
			Nodes scopeVariableSet = startNode.query(
					"child::bpel:variables/bpel:variable", CONTEXT);
			for (Node variable : scopeVariableSet) {
				String name = new NodeHelper(variable).getAttribute("name");

				if (name.equals(variableName))
					return variable;
			}

			throw new NavigationException("Variable not in process");
		}

		return getVariableByName(
				startNode.query("parent::*").get(
						startNode.query("parent::*").size() - 1), variableName);
	}
}
