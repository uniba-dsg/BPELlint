package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.PrefixHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;
import java.util.Map;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00048Validator extends Validator {
	public SA00048Validator(BpelProcessFiles files, ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes invokes = fileHandler.getBpel().getDocument().query("//bpel:invoke", CONTEXT);
		for (Node invoke : invokes) {
			try {
				Map<String, Node> messages = getCorrespondingMessages(invoke);
				Node variableForInput = getInputVariable(invoke);
				Node variableForOutput = getOutputVariable(invoke);

				if (variableForInput != null) {
					if (!hasCorrespondingMessage(variableForInput, messages.get("input")))
						reportViolation(invoke, 1);
				}

				if (variableForOutput != null) {
					if (!hasCorrespondingMessage(variableForOutput, messages.get("output"))) {
						reportViolation(invoke, 2);
					}
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
	}

	private Node getInputVariable(Node invokeActivity) {
		String inputVariableName = new NodeHelper(invokeActivity).getAttributeByName("inputVariable");
		return correspondingVariable(invokeActivity, inputVariableName);
	}

	private Node getOutputVariable(Node invokeActivity) {
		String outputVariableName = new NodeHelper(invokeActivity).getAttributeByName("outputVariable");
		return correspondingVariable(invokeActivity, outputVariableName);
	}

	private Map<String, Node> getCorrespondingMessages(Node invokeActivity)
			throws NavigationException {
		List<DocumentEntry> wsdlImports = fileHandler.getAllWsdls();
		Node operation;
		operation = navigator.correspondingOperation(invokeActivity);
		return navigator.getOperationMessages(wsdlImports, operation);
	}

	private void reportViolation(Node node, int type) {
		addViolation(getBpelFileName(), node, type);
	}

	private boolean hasCorrespondingMessage(Node variable, Node operationMessage) {
		String messageTypeQName = getAttributeValue(variable.query("@messageType"));
		String typeQName = getAttributeValue(variable.query("@type"));

		if (!messageTypeQName.isEmpty()) {
			Node variableMessage = getVariableMessage(messageTypeQName, variable);
			if (equalsMessage(variableMessage, operationMessage)) {
				return true;
			}
		} else if (typeQName != null) {
			if (messageHasOnlyOnePartWithElement(operationMessage)) {
				Node messagePartXsdType = findMessageXsdElement(operationMessage);
				Node variableXsdType = findXsdType(typeQName, variable);
				if (xsdTypeMatches(variableXsdType, messagePartXsdType)) {
					return true;
				}
			}
		}
		return false;
	}

	private Node findMessageXsdElement(Node operationMessage) {
		Node messagePartElement = getMessagePartAttributeElement(operationMessage);
		Node xsdElement = findXsdType(messagePartElement.getValue(), messagePartElement);
		String elementTypeQName = getAttributeValue(xsdElement.query("@type"));
		return findXsdType(elementTypeQName, messagePartElement);
	}

	private boolean messageHasOnlyOnePartWithElement(Node operationMessage) {
		return operationMessage.query("child::*").size() == 1
				&& operationMessage.query("child::wsdl:part[position()=1]/@element", CONTEXT)
						.size() == 1;
	}

	private Node getMessagePartAttributeElement(Node operationMessage) {
		Nodes partElement = operationMessage.query("child::wsdl:part[position()=1]/@element",
				CONTEXT);
		return (partElement.size() > 0) ? partElement.get(0) : null;
	}

	private boolean xsdTypeMatches(Node xsdType, Node xsdSecType) {
		return getAttributeValue(xsdType.query("@name")).equals(
				getAttributeValue(xsdSecType.query("@name")));
	}

	private Node findXsdType(String typeQName, Node variable) {
		String variableTypeNamespaceURI = variable.getDocument().getRootElement()
				.getNamespaceURI(PrefixHelper.getPrefix(typeQName));
		String xsdTypeName = PrefixHelper.removePrefix(typeQName);
		Node xsdType = null;

		for (Node node : fileHandler.getXsdSchema()) {
			if (new NodeHelper(node).hasTargetNamespace(variableTypeNamespaceURI)) {
				Nodes xsdTypes = node.getDocument().query("//*[@name='" + xsdTypeName + "']", CONTEXT);
				if (xsdTypes.size() > 0) {
					xsdType = xsdTypes.get(0);
					break;
				}
			}
		}
		return xsdType;
	}

	private boolean equalsMessage(Node firstNode, Node secondNode) {
		return equalsTargetNamespace(firstNode, secondNode)
				&& equalsMessageName(firstNode, secondNode);
	}

	private boolean equalsTargetNamespace(Node firstNode, Node secondNode) {
		return new NodeHelper(firstNode).hasTargetNamespace(secondNode);
	}

	private boolean equalsMessageName(Node firstNode, Node secondNode) {
		return (getAttributeValue(firstNode.query("@name")).equals(getAttributeValue(secondNode
				.query("@name"))));
	}

	private Node getVariableMessage(String messageTypeQName, Node variable) {
		String namespaceURI = variable.getDocument().getRootElement()
				.getNamespaceURI(PrefixHelper.getPrefix(messageTypeQName));
		String messageName = PrefixHelper.removePrefix(messageTypeQName);

		return navigator.getMessage(messageName, namespaceURI,
				fileHandler.getAllWsdls());
	}

	private Node correspondingVariable(Node invokeActivity, String variableName) {
		Nodes variable = invokeActivity.query("(ancestor::*/bpel:variables/bpel:variable[@name='"
				+ variableName + "'])[last()]", CONTEXT);

		if (variable.size() > 0)
			return variable.get(0);
		else
			return null;
	}

	@Override
	public int getSaNumber() {
		return 48;
	}

}
