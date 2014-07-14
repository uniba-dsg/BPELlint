package bpellint.tool.validators.rules;

import static bpellint.model.Standards.CONTEXT;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import nu.xom.Node;
import nu.xom.Nodes;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.PrefixHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.SimpleTypeChecker;
import bpellint.model.bpel.CorrelationElement;
import bpellint.model.bpel.CorrelationSetElement;
import bpellint.model.bpel.mex.MessageActivity;
import bpellint.model.bpel.var.CopyEntityImpl;
import bpellint.model.wsdl.MessageElement;
import bpellint.model.wsdl.PartElement;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;
import bpellint.tool.validators.result.ValidationCollector;

public class SA00021Validator extends Validator {

	private static final int MISTYPED_PROPERTY_ALIAS = 1;
	private static final int NO_PROPERTY_ALIAS = 2;

	public SA00021Validator(ProcessContainer files, ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		validateFor("//bpel:from[@property]");
		validateFor("//bpel:to[@property]");
		validateCorrelations();
	}

	private void validateCorrelations() {
		Map<MessageElement, List<PropertyAliasElement>> messageAliases = identifyMessageAliases();

		for (CorrelationElement correlation : processContainer.getAllCorrelations()) {
			try {
				CorrelationSetElement correlationSet = correlation.getCorrelationSet(correlation
						.getEnclosingScope());
				MessageElement message = correspondingMessage(correlation);
				List<PropertyElement> correlationProperties = correlationSet.getProperties();
				List<PropertyAliasElement> propertyAliases = messageAliases.get(message);
				if (propertyAliases == null) {
					addViolation(correlation, NO_PROPERTY_ALIAS);
				} else if (!match(correlationProperties, propertyAliases)) {
					addViolation(correlation, MISTYPED_PROPERTY_ALIAS);
				}
			} catch (NavigationException e) {
				/*
				 * Cannot validate because an element is missing correlationSet,
				 * operation, or input/output, otherwise it is missing an XML
				 * schema file,
				 */
			}
		}
	}

	private MessageElement correspondingMessage(CorrelationElement correlation)
			throws NavigationException {
		MessageActivity messageActivity = correlation.getCorrespondingMessageActivity();
		MessageElement message;
		if (messageActivity.isReceiving()) {
			message = messageActivity.getOperation().getInput().getMessage();
		} else {
			message = messageActivity.getOperation().getOutput().getMessage();
		}
		return message;
	}

	private boolean match(List<PropertyElement> correlationSetProperties,
			List<PropertyAliasElement> messageProperties) throws NavigationException {
		for (PropertyElement correlationProperty : correlationSetProperties) {
			for (PropertyAliasElement propertyAlias : messageProperties) {
				// TODO remove warning if queries are checked
				warnIfQueryIsChildOf(propertyAlias);

				PartElement part;
				try {
					if (!propertyAlias.getProperty().equals(correlationProperty)) {
						continue;
					}
					part = getCorrepondingMessagePart(propertyAlias);
				} catch (NavigationException e) {
					// There is no property or no part for this alias. Thus,
					// this is not the alias we are looking for.
					continue;
				}

				String propertyType = PrefixHelper.removePrefix(correlationProperty
						.getTypeAttribute());
				if (part.hasTypeAttribute()) {
					String type = part.getTypeAttribute();
					if (new SimpleTypeChecker(processContainer, part).isSimpleType(type)) {
						type = PrefixHelper.removePrefix(type);
						return type.equals(propertyType);
					}
				} else if (part.hasElement()) {
					Nodes elementNodes = part.toXOM().query(
							"//xsd:element[@name='" + PrefixHelper.removePrefix(part.getElement())
									+ "']", CONTEXT);
					// TODO query parsing
					if (!elementNodes.isEmpty()) {
						String elementType = new NodeHelper(elementNodes.get(0))
								.getAttribute("type");
						elementType = PrefixHelper.removePrefix(elementType).toLowerCase();
						return propertyType.equals(elementType);
					}
				}
			}
		}
		return false;
	}

	private void warnIfQueryIsChildOf(PropertyAliasElement propertyAlias) {
		if (!propertyAlias.toXOM().query("./vprop:query", CONTEXT).isEmpty()) {
			addWarning(propertyAlias, "<vprop:query>s are not parsed at the moment");
		}
	}

	private Map<MessageElement, List<PropertyAliasElement>> identifyMessageAliases() {
		Map<MessageElement, List<PropertyAliasElement>> aliasedMessages = new HashMap<>();
		for (PropertyAliasElement propertyAlias : processContainer.getAllPropertyAliases()) {
			try {
				MessageElement referencedMessage = propertyAlias.getReferencedMessage();
				List<PropertyAliasElement> aliases = aliasedMessages.get(referencedMessage);
				if (aliases == null) {
					aliases = new LinkedList<>();
					aliasedMessages.put(referencedMessage, aliases);
				}
				aliases.add(propertyAlias);
			} catch (NavigationException e) {
				// <message> undefined
			}
		}
		return aliasedMessages;
	}

	private void validateFor(String toOrFrom) {
		Nodes fromToSet = processContainer.getBpel().getDocument().query(toOrFrom, CONTEXT);
		for (Node fromTo : fromToSet) {
			try {
				hasCorrespondingPropertyAlias(fromTo);
			} catch (NavigationException e) {
				addViolation(fromTo);
			}
		}
	}

	private void hasCorrespondingPropertyAlias(Node fromTo) throws NavigationException {
		PropertyAliasElement propertyAlias = new CopyEntityImpl(fromTo, processContainer)
				.getVariablePropertyAlias();
		getCorrepondingMessagePart(propertyAlias);
	}

	private PartElement getCorrepondingMessagePart(PropertyAliasElement propertyAlias)
			throws NavigationException {
		String aliasPartName = PrefixHelper.removePrefix(propertyAlias.getPart());
		MessageElement message = propertyAlias.getReferencedMessage();
		String propertyType = PrefixHelper.removePrefix(propertyAlias.getProperty()
				.getTypeAttribute());

		for (PartElement part : message.getParts()) {
			if (!aliasPartName.equals(part.getNameAttribute())) {
				continue;
			}

			if (propertyType.equals(extractType(part))) {
				return part;
			}
		}

		throw new NavigationException("No part has matched.");
	}

	private String extractType(PartElement part) throws NavigationException {
		if (part.hasTypeAttribute()) {
			return PrefixHelper.removePrefix(part.getTypeAttribute());
		} else if (part.hasElement()) {
			return extractElementType(part);
		}
		return ".no.type.";
	}

	private String extractElementType(PartElement part) throws NavigationException {
		String name = PrefixHelper.removePrefix(part.getElement());
		Node element = processContainer.resolveName(targetNamespace(part), name, "xsd:element");

		String elementType = new NodeHelper(element).getAttribute("type");
		return PrefixHelper.removePrefix(elementType);
	}

	private String targetNamespace(PartElement part) throws NavigationException {
		return PrefixHelper.resolveQNameToNamespace(part.toXOM(), part.getElement());
	}

	@Override
	public int getSaNumber() {
		return 21;
	}

}
