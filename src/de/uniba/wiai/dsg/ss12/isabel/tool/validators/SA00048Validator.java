package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.prefixFree;

import java.util.HashMap;
import java.util.List;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00048Validator extends Validator {
	public SA00048Validator(BpelProcessFiles files, ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public boolean validate() {
		Nodes invokeNodes = fileHandler.getBpel().getDocument().query("//bpel:invoke", CONTEXT);
		for (Node invokeActivity : invokeNodes) {
			try {
				HashMap<String, Node> messages = getCorrespondingMessages(invokeActivity);
				Node variableForInput = getInputVariable(invokeActivity);
				Node variableForOutput = getOutputVariable(invokeActivity);

				if (variableForInput != null) {
					if (!hasCorrespondingMessage(variableForInput, messages.get("input")))
						reportViolation(invokeActivity, 1);
				}

				if (variableForOutput != null) {
					if (!hasCorrespondingMessage(variableForOutput, messages.get("output"))) {
						reportViolation(invokeActivity, 2);
					}
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
		return valid;
	}

	private Node getInputVariable(Node invokeActivity) {
		String inputVariableName = getAttributeValue(invokeActivity.query("@inputVariable"));
		return correspondingVariable(invokeActivity, inputVariableName);
	}

	private Node getOutputVariable(Node invokeActivity) {
		String outputVariableName = getAttributeValue(invokeActivity.query("@outputVariable"));
		return correspondingVariable(invokeActivity, outputVariableName);
	}

	private HashMap<String, Node> getCorrespondingMessages(Node invokeActivity)
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
		String elementTypeQname = getAttributeValue(xsdElement.query("@type"));
		return findXsdType(elementTypeQname, messagePartElement);
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
				.getNamespaceURI(navigator.getPrefix(typeQName));
		String xsdTypeName = prefixFree(typeQName);
		Nodes xsdTypes = null;
		Node xsdType = null;

		for (Node node : fileHandler.getXsdSchema()) {
			if (navigator.getTargetNamespace(node).equals(variableTypeNamespaceURI)) {
				xsdTypes = node.getDocument().query("//*[@name='" + xsdTypeName + "']", CONTEXT);
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
		return (navigator.getTargetNamespace(firstNode).equals(navigator
				.getTargetNamespace(secondNode)));
	}

	private boolean equalsMessageName(Node firstNode, Node secondNode) {
		return (getAttributeValue(firstNode.query("@name")).equals(getAttributeValue(secondNode
				.query("@name"))));
	}

	private Node getVariableMessage(String messageTypeQName, Node variable) {
		String namespaceURI = variable.getDocument().getRootElement()
				.getNamespaceURI(navigator.getPrefix(messageTypeQName));
		String messageName = prefixFree(messageTypeQName);
		Node variableMessage = navigator.getMessage(messageName, namespaceURI,
				fileHandler.getAllWsdls());

		return variableMessage;
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
