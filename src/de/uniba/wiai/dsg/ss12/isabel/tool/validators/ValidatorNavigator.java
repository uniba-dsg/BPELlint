package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.XSD_NAMESPACE;

import java.util.HashMap;
import java.util.List;

import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;

public class ValidatorNavigator {
	private final BpelProcessFiles fileHandler;

	public ValidatorNavigator(BpelProcessFiles fileHandler) {
		this.fileHandler = fileHandler;
	}

	public HashMap<String, Node> getOperationMessages(
			List<DocumentEntry> wsdlImports, Node operation)
			throws NavigationException {

		if (operation == null)
			return null;

		Nodes operationChildren = operation.query("child::*", CONTEXT);
		HashMap<String, Node> messages = null;
		if (operationChildren.size() > 0)
			messages = new HashMap<String, Node>();
		for (Node child : operationChildren) {

			String messageQName = getAttributeValue(child.query("@message"));
			String childName = getLocalName(child);
			String namespaceURI = operation.getDocument().getRootElement()
					.getNamespaceURI(getPrefix(messageQName));
			String messageName = prefixFree(messageQName);

			Node message = getMessage(messageName, namespaceURI, wsdlImports);
			if (message != null)
				messages.put(childName, message);
		}

		if (messages == null)
			throw new NavigationException("Messages are not defined.");

		return messages;
	}

	public Node getMessage(String messageName, String namespaceURI,
			List<DocumentEntry> wsdlImports) {
		Node message = null;
		for (DocumentEntry wsdlEntry : wsdlImports) {
			String targetNamespace = wsdlEntry.getTargetNamespace();
			if (targetNamespace.equals(namespaceURI)) {
				Nodes messageNodes = wsdlEntry.getDocument().query(
						"//wsdl:message[@name='" + messageName + "']", CONTEXT);

				if (messageNodes.size() > 0) {
					message = messageNodes.get(0);
					break;
				}
			}
		}

		return message;
	}

	public String getLocalName(Node node) {
		Element el = (Element) node;
		return el.getLocalName();
	}

	public String getTargetNamespace(Node node) {
		return getAttributeValue(node.getDocument().getRootElement()
				.query("@targetNamespace"));
	}

	public static String prefixFree(String localAttribute) {
		if (localAttribute.contains(":")) {
			localAttribute = localAttribute.substring(localAttribute
					.indexOf(":") + 1);
		}
		return localAttribute;
	}

	public String getPrefix(String value) {
		String[] qName = value.split(":");
		if (qName.length > 1) {
			return qName[0];
		} else {
			return "";
		}
	}

	public String getPrefixNamespaceURI(Document document,
			String namespacePrefix) throws NavigationException {
		if (namespacePrefix.isEmpty()) {
			throw new NavigationException(
					"Document has no namespace for this prefix");
		}

		return document.getRootElement().getNamespaceURI(namespacePrefix);
	}

	public Node correspondingOperation(Node messageActivity)
			throws NavigationException {
		Element invokeElement = (Element) messageActivity;
		String partnertLinkName = invokeElement
				.getAttributeValue("partnerLink");
		String operationName = invokeElement.getAttributeValue("operation");
		Node partnerLink = getPartnerLink(messageActivity.getDocument(),
				partnertLinkName);

		Node portType = partnerLinkToPortType(partnerLink);
		Node operation = portTypeToOperation(portType, operationName);

		return operation;
	}

	public Node getPartnerLink(Document document, String partnertLink)
			throws NavigationException {
		Nodes partnerLink = document.query(
				"//bpel:partnerLinks/bpel:partnerLink[@name='" + partnertLink
						+ "']", CONTEXT);

		if (partnerLink.size() > 0)
			return partnerLink.get(0);

		throw new NavigationException("PartnerLink not defined");
	}

	public Node partnerLinkToPortType(Node partnerLinkNode)
			throws NavigationException {
		Element partnerLink = (Element) partnerLinkNode;

		String partnerLinkTypeAttribute = partnerLink
				.getAttributeValue("partnerLinkType");
		String wsdlImportNamespace = getPrefixNamespaceURI(
				partnerLink.getDocument(), getPrefix(partnerLinkTypeAttribute));
		Document correspondingWsdlDom = searchedTargetNamespaceToWsdlDocument(wsdlImportNamespace);

		if (correspondingWsdlDom != null) {
			String partnerLinkTypeName = prefixFree(partnerLinkTypeAttribute);
			String partnerRoleName = partnerLink
					.getAttributeValue("partnerRole");
			String myRoleName = partnerLink.getAttributeValue("myRole");

			Nodes partnerRolePortType = correspondingWsdlDom.query(
					"//plink:partnerLinkType[@name='" + partnerLinkTypeName
							+ "']/" + "plink:role[@name='" + partnerRoleName
							+ "']/@portType", CONTEXT);

			if (partnerRolePortType.size() > 0) {
				String portTypeQName = partnerRolePortType.get(0).getValue();
				String portTypeNamespaceURI = getPrefixNamespaceURI(
						correspondingWsdlDom, getPrefix(portTypeQName));
				return getPortype(portTypeQName, portTypeNamespaceURI);
			} else {
				Nodes myRolePortType = correspondingWsdlDom.query(
						"//plink:partnerLinkType[@name='" + partnerLinkTypeName
								+ "']/" + "plink:role[@name='" + myRoleName
								+ "']/@portType", CONTEXT);
				if (myRolePortType.size() > 0) {
					String portTypeQName = myRolePortType.get(0).getValue();
					String portTypeNamespaceURI = getPrefixNamespaceURI(
							correspondingWsdlDom, getPrefix(portTypeQName));
					return getPortype(portTypeQName, portTypeNamespaceURI);
				}
			}
		}

		throw new NavigationException("PortType not defined");
	}

	public Document searchedTargetNamespaceToWsdlDocument(
			String searchedTargetNamespace) throws NavigationException {
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			String targetNamespace = wsdlEntry.getTargetNamespace();
			if (targetNamespace.equals(searchedTargetNamespace)) {
				return wsdlEntry.getDocument();
			}
		}

		throw new NavigationException("Document not existant");
	}

	public Node portTypeToOperation(Node portType, String operationName)
			throws NavigationException {
		Nodes operations = portType.query(
				"child::wsdl:operation[attribute::name='" + operationName
						+ "']", CONTEXT);
		if (operations.size() > 0)
			return operations.get(0);

		throw new NavigationException("Operation not defined");
	}

	public Node getPortype(String portTypeQName, String portTypeNamespaceURI)
			throws NavigationException {
		String portTypeName = prefixFree(portTypeQName);
		for (DocumentEntry wsdlEntry : fileHandler.getAllWsdls()) {
			String targetNamespace = wsdlEntry.getTargetNamespace();
			if (targetNamespace.equals(portTypeNamespaceURI)) {
				Nodes portTypes = wsdlEntry.getDocument().query(
						"//wsdl:portType[@name='" + portTypeName + "']",
						CONTEXT);
				if (portTypes.size() > 0) {
					return portTypes.get(0);
				}
			}
		}

		throw new NavigationException("portType not defined");
	}

	public Node operationToMessage(List<DocumentEntry> wsdlImports,
			Node operation) throws NavigationException {

		if (operation == null)
			throw new NavigationException("Operation not defined");

		Nodes inputNodes = operation.query("child::wsdl:input/@message",
				CONTEXT);


		if (inputNodes.size() <= 0) {
			return null;
		}

		String messageQName = inputNodes.get(0).getValue();
		String qNamePrefix = getPrefix(messageQName);
		String messageName = prefixFree(messageQName);
		String messageNamespaceURI = getPrefixNamespaceURI(
				operation.getDocument(), qNamePrefix);

		Node message = null;
		for (DocumentEntry wsdlEntry : wsdlImports) {
			String targetNamespace = wsdlEntry.getTargetNamespace();
			if (targetNamespace.equals(messageNamespaceURI)) {
				Nodes messageNodes = wsdlEntry.getDocument().query(
						"//wsdl:message[@name='" + messageName + "']", CONTEXT);

				if (messageNodes.size() > 0) {
					message = messageNodes.get(0);
					break;
				}

			}
		}

		return message;
	}

	public Node getCorrespondingOutgoingMessage(Node invoke)
			throws NavigationException {

		Node operation = correspondingOperation(invoke);
		String messageAttribute = getAttributeValue(operation.query(
				"wsdl:output/@message", CONTEXT));
		Document correspondingWsdl = operation.getDocument();
		Nodes messages = correspondingWsdl.query("//wsdl:message", CONTEXT);
		for (Node message : messages) {
			String messageName = getAttributeValue(message.query("@name",
					CONTEXT));
			if (messageName.equals(prefixFree(messageAttribute))) {
				return message;
			}
		}
		throw new NavigationException("corresponding <message> is not defined");
	}

	public Node getCorrespondingIncomingMessage(Node invoke)
			throws NavigationException {

		Node operation = correspondingOperation(invoke);
		String messageAttribute = getAttributeValue(operation.query(
				"wsdl:input/@message", CONTEXT));
		Document correspondingWsdl = operation.getDocument();
		Nodes messages = correspondingWsdl.query("//wsdl:message", CONTEXT);
		for (Node message : messages) {
			String messageName = getAttributeValue(message.query("@name",
					CONTEXT));
			if (messageName.equals(prefixFree(messageAttribute))) {
				return message;
			}
		}
		throw new NavigationException("corresponding <message> is not defined");
	}

	public boolean hasInputVariable(Node msgActivity) {
		Nodes inputVar = msgActivity.query("attribute::inputVariable", CONTEXT);
		return inputVar.size() > 0;
	}

	public boolean hasOutputVariable(Node msgActivity) {
		Nodes outVar = msgActivity.query("attribute::outputVariable", CONTEXT);
		return outVar.size() > 0;
	}

	public DocumentEntry getCorrespondingWsdlToCorrelationSet(
			Node correlationSet) throws NavigationException {
		String namespacePrefix = getCorrelationPropertyAliasPrefix(correlationSet);
		String correspondingTargetNamespace = getImportNamespace(
				correlationSet, namespacePrefix);
		for (DocumentEntry wsdlFile : fileHandler.getAllWsdls()) {
			if (wsdlFile.getTargetNamespace().equals(
					correspondingTargetNamespace)) {

				return wsdlFile;
			}
		}
		throw new NavigationException("Referenced WSDL File does not exist.");
	}

	public String getCorrelationPropertyAliasPrefix(Node correlationSet)
			throws NavigationException {

		String propertyAliasName = getAttributeValue(correlationSet.query(
				"@properties", CONTEXT));
		String namespacePrefix = getPrefix(propertyAliasName);

		if (namespacePrefix != null) {
			return namespacePrefix;
		}

		throw new NavigationException(
				"<correlationSet>@properties prefix does not exist.");
	}

	public String getImportNamespace(Node node, String namespacePrefix) {
		Element rootElement = node.getDocument().getRootElement();

		try {
			return rootElement.getNamespaceURI(namespacePrefix);
		} catch (NullPointerException e) {
			/*
			 * if the prefix is undefined in the root element, getNamespaceURI
			 * will throw a nullPointerException.
			 */
			return "";
		}
	}

	public Node getCorrespondingPropertyAlias(Node correlationSet,
			Document wsdlFile) throws NavigationException {
		Nodes propertyAliases = wsdlFile
				.query("//vprop:propertyAlias", CONTEXT);
		String propertyAliasAttribute = prefixFree(getAttributeValue(correlationSet
				.query("@properties", CONTEXT)));

		return navigateFromPropertyAliasNameToPropertyAlias(propertyAliases,
				propertyAliasAttribute);
	}

	public Node navigateFromPropertyAliasNameToPropertyAlias(Nodes aliases,
			String propertyAliasAttribute) throws NavigationException {
		for (Node propertyAlias : aliases) {
			String propertyAliasName = prefixFree(getAttributeValue(propertyAlias
					.query("@propertyName", CONTEXT)));

			if (propertyAliasName.equals(propertyAliasAttribute)) {
				return propertyAlias;
			}
		}
		throw new NavigationException(
				"Referenced <propertyAlias> does not exist.");
	}

	public Node getCorrespondingProperty(Node propertyAlias)
			throws NavigationException {
		Document wsdlDom = propertyAlias.getDocument();
		String propertyAttribute = getAttributeValue(propertyAlias.query(
				"@propertyName", CONTEXT));
		Nodes properties = wsdlDom.query("//vprop:property", CONTEXT);

		for (Node property : properties) {
			String propertyName = getAttributeValue(property.query("@name",
					CONTEXT));

			if (propertyName.equals(prefixFree(propertyAttribute))) {
				return property;
			}
		}
		throw new NavigationException("Referenced <property> does not exist.");
	}

	public Document getXmlSchema() throws NavigationException {
		for (DocumentEntry documentEntry : fileHandler.getAllXsds()) {
			if (XSD_NAMESPACE.equals(documentEntry.getTargetNamespace())) {
				return documentEntry.getDocument();
			}
		}
		throw new NavigationException(
				"XMLSchema should have been imported, but haven't.");
	}

	public static String getAttributeValue(Nodes attributes) {
		Node attribute;
		if (attributes.size() > 0) {
			attribute = attributes.get(0);
			if (attribute instanceof Attribute) {
				return attribute.getValue();
			}
		}
		return "";
	}
}
