package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.PrefixHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;

public class SA00010Validator extends Validator {

	public SA00010Validator(BpelProcessFiles files,
	                        ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		String filePath = fileHandler.getBpel().getFilePath();

		for (Node node : allBpelNodes()) {
			if (!typeDefinitionExists(node)) {
				addViolation(filePath, node, 1);
			}
		}
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
				return "".equals(variableType) || isInAnyXsd("element", variableType);

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
		String attributeValue = new NodeHelper(node).getAttributeByName(definitionType);
		return PrefixHelper.removePrefix(attributeValue);
	}

	private boolean isInAnyWsdl(String definitionType, String type) {
		return inAnyFile(definitionType, type, fileHandler.getAllWsdls());
	}

	private boolean inAnyFile(String definitionType, String type,
	                          List<DocumentEntry> files) {
		for (DocumentEntry domEntry : files) {
			Nodes rightNamedElements = domEntry.getDocument().query(
					"//*[@name=\"" + type + "\"]", CONTEXT);

			for (Node rightElement : rightNamedElements) {
				String definitionElement = toElement(rightElement)
						.getQualifiedName();

				if (definitionType.equals(PrefixHelper.removePrefix(definitionElement))) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean isInAnyXsd(String definitionType, String type) {
		for (Node domEntry : fileHandler.getXsdSchema()) {
			Nodes rightNamedElements = domEntry.getDocument().query(
					"//*[@name=\"" + type + "\"]", CONTEXT);

			for (Node rightElement : rightNamedElements) {
				String definitionElement = toElement(rightElement)
						.getQualifiedName();

				if (definitionType.equals(PrefixHelper.removePrefix(definitionElement))) {
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
