package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.prefixFree;

import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00010Validator extends Validator {

	private String filePath;

	public SA00010Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		filePath = fileHandler.getBpel().getFilePath();

		for (Node node : allBpelNodes()) {
			if (!typeDefinitionExists(node)) {
				addViolation(filePath, node, 1);
			}
		}

		return valid;
	}

	private Nodes allBpelNodes() {
		return fileHandler.getBpel().getDocument().query("//bpel:*", CONTEXT);
	}

	private boolean typeDefinitionExists(Node node) {
		String elementName = toElement(node).getQualifiedName();

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
			if (!"".equals(variableType)) {
				return isInAnyXsd("element", variableType);
			}
			return true;

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
			if (!"".equals(catchType)) {
				return isInAnyXsd("element", catchType);
			}
			return true;

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
			if (!"".equals(toType)) {
				return isInAnyWsdl("property", toType);
			}
			return true;
		case "from":
			String fromType = getType(node, "property");
			if (!"".equals(fromType)) {
				return isInAnyWsdl("property", fromType);
			}
			return true;
		default:
			return true;
		}
	}

	private String getType(Node node, String definitionType) {
		String arttributeValue = getAttributeValue(node.query("@"
				+ definitionType, CONTEXT));
		return prefixFree(arttributeValue);
	}

	private boolean isInAnyWsdl(String definitionType, String type) {
		return inAnyFile(definitionType, type, fileHandler.getAllWsdls());
	}

	private boolean inAnyFile(String definitionType, String type,
			List<DocumentEntry> files) {
		for (DocumentEntry domEntry : files) {
			Nodes rigthNamedElements = domEntry.getDocument().query(
					"//*[@name=\"" + type + "\"]", CONTEXT);

			for (Node rigthElement : rigthNamedElements) {
				String definitionElement = toElement(rigthElement)
						.getQualifiedName();

				if (definitionType.equals(prefixFree(definitionElement))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInAnyXsd(String definitionType, String type) {
		for (Node domEntry : fileHandler.getXsdSchema()) {
			Nodes rigthNamedElements = domEntry.getDocument().query(
					"//*[@name=\"" + type + "\"]", CONTEXT);

			for (Node rigthElement : rigthNamedElements) {
				String definitionElement = toElement(rigthElement)
						.getQualifiedName();

				if (definitionType.equals(prefixFree(definitionElement))) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public int getSaNumber() {
		return 10;
	}

}
