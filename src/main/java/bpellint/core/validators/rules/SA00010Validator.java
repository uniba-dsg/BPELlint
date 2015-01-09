package bpellint.core.validators.rules;

import static bpellint.core.model.Standards.CONTEXT;

import java.util.List;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.XmlFile;
import bpellint.core.model.bpel.mex.MessageActivity;
import bpellint.core.model.bpel.mex.MessageActivityImpl;
import bpellint.core.model.bpel.mex.OnEventElement;
import bpellint.core.model.wsdl.OperationOverloadException;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00010Validator extends Validator {

	public SA00010Validator(ProcessContainer files,
			SimpleValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node node : allBpelNodes()) {
			if (!typeDefinitionExists(node)) {
				addViolation(node);
			}
		}
	}

	private Nodes allBpelNodes() {
		return processContainer.getBpel().getDocument()
				.query("//bpel:*", CONTEXT);
	}

	private boolean typeDefinitionExists(Node element) {
		NodeHelper nodeHelper = new NodeHelper(element);

		switch (nodeHelper.getLocalName()) {
		case "partnerLink":
			String partnerLinkType = getType(nodeHelper, "partnerLinkType");
			return isInAnyWsdl("partnerLinkType", partnerLinkType);

		case "variable":
			String variableType = getType(nodeHelper, "messageType");
			if (!"".equals(variableType)) {
				return isInAnyWsdl("message", variableType);
			}

			variableType = getType(nodeHelper, "type");
			if (!"".equals(variableType)) {
				return isInAnyXsd("simpleType", variableType) || isInAnyXsd("complexType", variableType);
			}

			variableType = getType(nodeHelper, "element");
			return "".equals(variableType) || isInAnyXsd("element", variableType);

		case "correlationSet":
			String correlationSetType = getType(nodeHelper, "properties");
			return isInAnyWsdl("property", correlationSetType);

		case "catch":
			String catchType = getType(nodeHelper, "faultMessageType");
			if (!"".equals(catchType)) {
				return isInAnyWsdl("message", catchType);
			}

			catchType = getType(nodeHelper, "faultElement");
			return "".equals(catchType) || isInAnyXsd("element", catchType);

		case "onEvent":
			boolean onEventValid = true;
			String onEventVariableType = getType(nodeHelper, "messageType");
			if (!"".equals(onEventVariableType)) {
				onEventValid = isInAnyWsdl("message", onEventVariableType);
			}

			onEventVariableType = getType(nodeHelper, "element");
			if (!"".equals(onEventVariableType)) {
				onEventValid = onEventValid && isInAnyXsd("element", onEventVariableType);
			}

			if(!onEventValid){
				return false;
			}
			// fall through as onEvent is also a message activitiy
		case "reply":
		case "receive":
		case "invoke":
		case "onMessage":
			String messageActivityType;
			try {
				messageActivityType = getPortType(nodeHelper);
			} catch (NavigationException e) {
				return false;
			}
			return isInAnyWsdl("portType", messageActivityType);

		case "to":
		case "from":
			String copyEntityType = getType(nodeHelper, "property");
			return "".equals(copyEntityType) || isInAnyWsdl("property", copyEntityType);
		default:
			return true;
		}
	}

	private String getPortType(NodeHelper nodeHelper) throws NavigationException {
		MessageActivity messageActivity;
		if ("onEvent".equals(nodeHelper.getLocalName())) {
			messageActivity = new OnEventElement(nodeHelper.toXOM(), processContainer);
		} else {
			messageActivity = new MessageActivityImpl(nodeHelper, processContainer);
		}
		try {
			return messageActivity.getOperation().getPortType().getName();
		} catch (OperationOverloadException overloadedOperations) {
			return overloadedOperations.getOperations().get(0).getPortType().getName();
		}
	}

	private String getType(NodeHelper node, String definitionType) {
		String attributeValue = node.getAttribute(definitionType);
		return PrefixHelper.removePrefix(attributeValue);
	}

	private boolean isInAnyWsdl(String definitionType, String type) {
		return inAnyFile(definitionType, type, processContainer.getWsdls());
	}

	private boolean inAnyFile(String definitionType, String type,
			List<XmlFile> files) {
		for (XmlFile domEntry : files) {
			String query = "//*[@name='" + type + "']";
			Nodes rightNamedElements = domEntry.getDocument().query(
					query, CONTEXT);

			if (isContained(definitionType, rightNamedElements)) {
				return true;
			}
		}
		return false;
	}

	private boolean isContained(String definitionType, Nodes rightNamedElements) {
		for (Node rightElement : rightNamedElements) {
			String definitionElement = NodeHelper.toElement(rightElement)
					.getQualifiedName();
			if (definitionType.equals(PrefixHelper
					.removePrefix(definitionElement))) {
				return true;
			}
		}
		return false;
	}

	private boolean isInAnyXsd(String definitionType, String type) {
		for (Node domEntry : processContainer.getSchemas()) {
			Nodes rightNamedElements = domEntry.getDocument().query(
					"//*[@name='" + type + "']", CONTEXT);
			if (isContained(definitionType, rightNamedElements)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int getSaNumber() {
		return 10;
	}

}
