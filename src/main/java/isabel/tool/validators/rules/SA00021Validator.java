package isabel.tool.validators.rules;

import isabel.model.*;
import isabel.model.bpel.var.CopyEntityImpl;
import isabel.model.wsdl.MessageElement;
import isabel.model.wsdl.PartElement;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.tool.validators.result.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00021Validator extends Validator {

	public SA00021Validator(ProcessContainer files, ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		validateFor("//bpel:from[@property]");
		validateFor("//bpel:to[@property]");
	}

	private void validateFor(String toOrFrom) {
		Nodes fromToSet = processContainer.getBpel().getDocument().query(toOrFrom, CONTEXT);
		for (Node fromTo : fromToSet) {
			try {
				//TODO remove
				if (new NodeHelper(fromTo).getFilePath().endsWith("OnEvent-Variable-Undefined.bpel")) {
					System.out.println("");
				}
				hasCorrespondingPropertyAlias(fromTo);
			} catch (NavigationException e) {
				addViolation(fromTo);
			}
		}
	}

	private void hasCorrespondingPropertyAlias(Node fromTo) throws NavigationException {
		PropertyAliasElement propertyAlias = new CopyEntityImpl(fromTo, processContainer)
				.getVariablePropertyAlias();
		String aliasPartName = PrefixHelper.removePrefix(propertyAlias.getPart());
		MessageElement message = propertyAlias.getReferencedMessage();
		String propertyType = PrefixHelper.removePrefix(propertyAlias.getProperty().getTypeAttribute());

		for (PartElement part : message.getParts()) {
			if (!aliasPartName.equals(part.getNameAttribute())) {
				continue;
			}

			if (propertyType.equals(extractType(part))) {
				return;
			}
		}

		throw new NavigationException("No part has matched.");
	}

	private String extractType(PartElement part) throws NavigationException {
		if (part.hasTypeAttribute()) {
			return	PrefixHelper.removePrefix(part.getTypeAttribute());
		} else if (part.hasElement()) {
			return extractElementType(part);
		}
		return ".no.type.";
	}

	private String extractElementType(PartElement part) throws NavigationException {
		String name = PrefixHelper.removePrefix(part.getElement());
		Node element = processContainer.resolveName(targetNamespace(part), name,"xsd:element");

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
