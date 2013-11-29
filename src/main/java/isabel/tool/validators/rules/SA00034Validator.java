package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;
import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.NodesUtil;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00034Validator extends Validator {

	public SA00034Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<Node> fromTos = getFroms();
		fromTos.addAll(getTos());

		checkPartAttributeUsage(fromTos);
	}

	private List<Node> getTos() {
		Nodes toNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:to[@variable]", CONTEXT);
		return NodesUtil.toList(toNodes);
	}

	private List<Node> getFroms() {
		Nodes fromNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:copy/bpel:from[@variable]", CONTEXT);
		return NodesUtil.toList(fromNodes);
	}

	private void checkPartAttributeUsage(List<Node> fromTos) {
		for (Node fromToNode : fromTos) {
			NodeHelper fromTo = new NodeHelper(fromToNode);
			String variableName = fromTo.getAttribute("variable");
			if (!isCorrespondingVariableOfMessageType(fromToNode, variableName)
					&& fromTo.hasAttribute("part")) {
				addViolation(fromToNode, 1);
			}
		}
	}

	private boolean isCorrespondingVariableOfMessageType(Node fromTo,
			String variableName) {
		try {
			Node variableNode = navigator.getVariableByName(fromTo,
					variableName);
			NodeHelper variable = new NodeHelper(variableNode);
			if ("catch".equals(variable.getLocalName())) {
				return variable.hasAttribute("faultMessageType");
			}
	
			return variable.hasAttribute("messageType");
		} catch (NavigationException e) {
			addViolation(fromTo, 2);
			return false;
		}
	}

	@Override
	public int getSaNumber() {
		return 34;
	}

}
