package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.BPELHelper;
import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;

public class SA00021Validator extends Validator {

	public SA00021Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		hasEachCorrelationSetExistingProperty();
		validateFor("//bpel:from[@property]");
		validateFor("//bpel:to[@property]");
	}

	private void validateFor(String toOrFrom) {
		Nodes fromToSet = fileHandler.getBpel().getDocument()
				.query(toOrFrom, CONTEXT);
		for (Node fromTo : fromToSet) {
			String property = new NodeHelper(fromTo).getAttribute("property");
			try {
				Node variable = getCorrespondingVariable(fromTo);
				hasCorrespondingPropertyAlias(variable, property, fromTo);
			} catch (NavigationException e) {
				addViolation(fromTo);
			}
		}
	}

	private Node getCorrespondingVariable(Node fromTo)
			throws NavigationException {
		return BPELHelper.getVariableByName(fromTo,
				new NodeHelper(fromTo).getAttribute("variable"));
	}

	private void hasCorrespondingPropertyAlias(Node node, String property,
			Node partHolder) throws NavigationException {
		String type = "";

		if (NodeHelper.toElement(node).getLocalName().equals("onEvent")) {
			type = getOnEventVariableType(node);
		} else if (NodeHelper.toElement(node).getLocalName().equals("variable")) {
			type = getEnclosingScopeVariableType(node);
		}

		if (!type.isEmpty()) {
			Document wsdl = getCorrespondingWsdl(property, node);
			Nodes wsdlPropertyAliasSet = wsdl.query("//vprop:propertyAlias",
					CONTEXT);
			for (Node propertyAlias : wsdlPropertyAliasSet) {
				String propertyName = PrefixHelper.removePrefix(new NodeHelper(
						propertyAlias).getAttribute("propertyName"));

				if (PrefixHelper.removePrefix(property).equals(propertyName)) {
					if (!(isOfThisMessageType(type, propertyAlias, partHolder)
							|| isOfThisType(type, propertyAlias) || isOfThisElement(
								type, propertyAlias))) {
						addViolation(node);
					}
				} else {
					addViolation(node);
				}
			}
		}
	}

	private boolean isOfThisElement(String type, Node propertyAlias) {
		return new NodeHelper(propertyAlias).getAttribute("element").equals(
				PrefixHelper.removePrefix(type));
	}

	private boolean isOfThisType(String type, Node propertyAlias) {
		return new NodeHelper(propertyAlias).getAttribute("type").equals(
				PrefixHelper.removePrefix(type));
	}

	private boolean isOfThisMessageType(String type, Node propertyAlias,
			Node partHolder) {
		NodeHelper nodeHelper = new NodeHelper(propertyAlias);
		String messageType = PrefixHelper.removePrefix(nodeHelper
				.getAttribute("messageType"));
		boolean isMessageType = messageType.equals(PrefixHelper
				.removePrefix(type));

		if (isMessageType) {
			return (new NodeHelper(partHolder).hasSameAttribute(nodeHelper,
					"part") || hasOneMessagePart(propertyAlias, messageType));
		} else {
			return true;
		}
	}

	private boolean hasOneMessagePart(Node propertyAlias, String messageType) {
		return propertyAlias
				.getDocument()
				.query("//wsdl:message[@name=\"" + messageType
						+ "\"]/wsdl:part", CONTEXT).size() == 1;
	}

	private Document getCorrespondingWsdl(String property, Node node)
			throws NavigationException {
		String targetNamespace = navigator.getPrefixNamespaceURI(
				node.getDocument(), PrefixHelper.getPrefix(property));
		return fileHandler.getWsdlByTargetNamespace(targetNamespace);
	}

	private void hasEachCorrelationSetExistingProperty() {
		Nodes correlationSets = fileHandler.getBpel().getDocument()
				.query("//bpel:correlationSet", CONTEXT);

		for (Node correlationSet : correlationSets) {
			try {
				XmlFile wsdl = navigator
						.getCorrespondingWsdlToCorrelationSet(correlationSet);
				Node propertyAlias = navigator.getCorrespondingPropertyAlias(
						correlationSet, wsdl.getDocument());
				navigator.getCorrespondingProperty(propertyAlias);
			} catch (NavigationException e) {
				addViolation(correlationSet);
			}
		}
	}

	private String getEnclosingScopeVariableType(Node variable)
			throws NavigationException {
		NodeHelper variableHelper = new NodeHelper(variable);
		if (variableHelper.hasAttribute("messageType")) {
			return variableHelper.getAttribute("messageType");
		} else if (variableHelper.hasAttribute("type")) {
			return variableHelper.getAttribute("type");
		} else if (variableHelper.hasAttribute("element")) {
			return variableHelper.getAttribute("element");
		} else {
			throw new NavigationException(
					"Node variable does not contain any messageType, type or element attribute");
		}
	}

	private String getOnEventVariableType(Node onEvent)
			throws NavigationException {

		NodeHelper onEventHelper = new NodeHelper(onEvent);
		if (onEventHelper.hasAttribute("messageType")) {
			return onEventHelper.getAttribute("messageType");
		} else if (onEventHelper.hasAttribute("element")) {
			return onEventHelper.getAttribute("element");
		} else {
			throw new NavigationException(
					"Node onEvent does not contain any messageType or element attribute");
		}
	}

	@Override
	public int getSaNumber() {
		return 21;
	}

}
