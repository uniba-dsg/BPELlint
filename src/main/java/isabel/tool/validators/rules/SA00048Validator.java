package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;
import java.util.Map;

import static isabel.tool.impl.Standards.CONTEXT;

public class SA00048Validator extends Validator {
	public SA00048Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes invokes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT);
		for (Node invoke : invokes) {
			try {
				Map<String, Node> messages = getCorrespondingMessages(invoke);
				Node variableForInput = getInputVariable(invoke);
				Node variableForOutput = getOutputVariable(invoke);

				if (variableForInput != null) {
					if (!hasCorrespondingMessage(variableForInput,
							messages.get("input")))
						addViolation(invoke, 1);
				}

				if (variableForOutput != null) {
					if (!hasCorrespondingMessage(variableForOutput,
							messages.get("output"))) {
						addViolation(invoke, 2);
					}
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
	}

	private Node getInputVariable(Node invokeActivity) {
		String inputVariableName = new NodeHelper(invokeActivity)
				.getAttribute("inputVariable");
		return correspondingVariable(invokeActivity, inputVariableName);
	}

	private Node getOutputVariable(Node invokeActivity) {
		String outputVariableName = new NodeHelper(invokeActivity)
				.getAttribute("outputVariable");
		return correspondingVariable(invokeActivity, outputVariableName);
	}

	private Map<String, Node> getCorrespondingMessages(Node invokeActivity)
			throws NavigationException {
		List<XmlFile> wsdlImports = fileHandler.getAllWsdls();
		Node operation;
		operation = navigator.correspondingOperation(invokeActivity);
		return navigator.getOperationMessages(wsdlImports, operation);
	}

	private boolean hasCorrespondingMessage(Node variable, Node operationMessage) {
		String messageTypeQName = new NodeHelper(variable)
				.getAttribute("messageType");
		String typeQName = new NodeHelper(variable).getAttribute("type");

		if (!messageTypeQName.isEmpty()) {
			Node variableMessage = getVariableMessage(messageTypeQName,
					variable);
			if (equalsMessage(variableMessage, operationMessage)) {
				return true;
			}
		} else if (typeQName != null) {
			if (messageHasOnlyOnePartWithElement(operationMessage)) {
				Node messagePartXsdType = findMessageXsdElement(operationMessage);
				Node variableXsdType = findXsdType(typeQName, variable);
				if (hasSameNameAttribute(variableXsdType, messagePartXsdType)) {
					return true;
				}
			}
		}
		return false;
	}

	private Node findMessageXsdElement(Node operationMessage) {
		Node messagePartElement = getMessagePartAttributeElement(operationMessage);
		Node xsdElement = findXsdType(messagePartElement.getValue(),
				messagePartElement);
		String elementTypeQName = new NodeHelper(xsdElement)
				.getAttribute("type");
		return findXsdType(elementTypeQName, messagePartElement);
	}

	private boolean messageHasOnlyOnePartWithElement(Node operationMessage) {
		return operationMessage.query("child::*").size() == 1
				&& operationMessage.query(
				"child::wsdl:part[position()=1]/@element", CONTEXT)
				.size() == 1;
	}

	private Node getMessagePartAttributeElement(Node operationMessage) {
		Nodes partElement = operationMessage.query(
				"child::wsdl:part[position()=1]/@element", CONTEXT);
		return (partElement.size() > 0) ? partElement.get(0) : null;
	}

	private boolean hasSameNameAttribute(Node xsdType, Node xsdSecType) {
		return new NodeHelper(xsdType).getAttribute("name").equals(
				new NodeHelper(xsdSecType).getAttribute("name"));
	}

	private Node findXsdType(String typeQName, Node variable) {
		String variableTypeNamespaceURI = variable.getDocument()
				.getRootElement()
				.getNamespaceURI(PrefixHelper.getPrefix(typeQName));
		String xsdTypeName = PrefixHelper.removePrefix(typeQName);
		Node xsdType = null;

		for (Node node : fileHandler.getXsdSchema()) {
			if (new NodeHelper(node)
					.hasTargetNamespace(variableTypeNamespaceURI)) {
				Nodes xsdTypes = node.getDocument().query(
						"//*[@name='" + xsdTypeName + "']", CONTEXT);
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
				&& hasSameNameAttribute(firstNode, secondNode);
	}

	private boolean equalsTargetNamespace(Node firstNode, Node secondNode) {
		return new NodeHelper(firstNode).hasTargetNamespace(secondNode);
	}

	private Node getVariableMessage(String messageTypeQName, Node variable) {
		String namespaceURI = variable.getDocument().getRootElement()
				.getNamespaceURI(PrefixHelper.getPrefix(messageTypeQName));
		String messageName = PrefixHelper.removePrefix(messageTypeQName);

		return navigator.getMessage(messageName, namespaceURI,
				fileHandler.getAllWsdls());
	}

	private Node correspondingVariable(Node invokeActivity, String variableName) {
		Nodes variable = invokeActivity.query(
				"(ancestor::*/bpel:variables/bpel:variable[@name='"
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
