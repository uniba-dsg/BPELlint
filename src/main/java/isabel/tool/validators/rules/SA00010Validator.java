package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class SA00010Validator extends Validator {

	public SA00010Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
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
		return fileHandler.getBpel().getDocument().query("//bpel:*", CONTEXT);
	}

	private boolean typeDefinitionExists(Node node) {
		String elementName = NodeHelper.toElement(node).getQualifiedName();

		switch (elementName) {
			case "partnerLink":
				String partnerLinkType = getType(node, "partnerLinkType");
				return isInAnyWsdl("partnerLinkType", partnerLinkType);

			case "variable":
				String variableType = getType(node, "messageType");
				if (!"".equals(variableType)) {
					return isInAnyWsdl("message", variableType);
				}

				variableType = getType(node, "type");
				if (!"".equals(variableType)) {
					return isInAnyXsd("simpleType", variableType)
							|| isInAnyXsd("complexType", variableType);
				}

				variableType = getType(node, "element");
				return "".equals(variableType)
						|| isInAnyXsd("element", variableType);

			case "correlationSet":
				String correlationSetType = getType(node, "properties");
				return isInAnyWsdl("property", correlationSetType);

			case "reply":
				String replyType = getType(node, "portType");
				return isInAnyWsdl("portType", replyType);

			case "catch":
				String catchType = getType(node, "faultMessageType");
				if (!"".equals(catchType)) {
					return isInAnyWsdl("message", catchType);
				}

				catchType = getType(node, "faultElement");
				return "".equals(catchType) || isInAnyXsd("element", catchType);

			case "receive":
				String receiveType = getType(node, "portType");
				return isInAnyWsdl("portType", receiveType);

			case "invoke":
				String invokeType = getType(node, "portType");
				return isInAnyWsdl("portType", invokeType);

			case "onMessage":
				String onMessageType = getType(node, "portType");
				return isInAnyWsdl("portType", onMessageType);

			case "onEvent":
				String onEventType = getType(node, "portType");
				return isInAnyWsdl("portType", onEventType);

			case "to":
				String toType = getType(node, "property");
				return "".equals(toType) || isInAnyWsdl("property", toType);
			case "from":
				String fromType = getType(node, "property");
				return "".equals(fromType) || isInAnyWsdl("property", fromType);
			default:
				return true;
		}
	}

	private String getType(Node node, String definitionType) {
		String attributeValue = new NodeHelper(node)
				.getAttribute(definitionType);
		return PrefixHelper.removePrefix(attributeValue);
	}

	private boolean isInAnyWsdl(String definitionType, String type) {
		return inAnyFile(definitionType, type, fileHandler.getWsdls());
	}

	private boolean inAnyFile(String definitionType, String type,
	                          List<XmlFile> files) {
		for (XmlFile domEntry : files) {
			Nodes rightNamedElements = domEntry.getDocument().query(
					"//*[@name='" + type + "']", CONTEXT);

			if (isContained(definitionType, rightNamedElements)){
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
		for (Node domEntry : fileHandler.getSchemas()) {
			Nodes rightNamedElements = domEntry.getDocument().query(
					"//*[@name='" + type + "']", CONTEXT);
			if (isContained(definitionType, rightNamedElements)){
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
