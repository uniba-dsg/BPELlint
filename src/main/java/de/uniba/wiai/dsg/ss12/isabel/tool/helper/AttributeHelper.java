package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

import nu.xom.Attribute;
import nu.xom.Element;

public class AttributeHelper {

	private final Attribute attribute;

	public AttributeHelper(Attribute attribute) {
		this.attribute = attribute;
	}

	public boolean isEqualTo(Attribute other) {
		// TODO debug logging
		// System.out.println("Comparing " + attribute.getValue() + " with " +
		// other.getValue());

		if (hasDifferentAttributeNamespace(other))
			return false;
		if (hasDifferentAttributeValue(other))
			return false;

		return true;
	}

	public boolean hasDifferentAttributeNamespace(Attribute other) {
		String namespaceA = ((Element) attribute.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(attribute.getValue()));
		String namespaceB = ((Element) other.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(other.getValue()));

		// TODO debug logging
		// System.out.println("Comparing namespaces [" + namespaceA + "] and ["
		// + namespaceB + "]");
		if (!namespaceA.equals(namespaceB)) {
			// TODO debug logging
			// System.out.println("Namespaces not identical");
			return true;
		}
		return false;
	}

	public boolean hasDifferentAttributeValue(Attribute other) {
		String valueA = PrefixHelper.removePrefix(attribute.getValue());
		String valueB = PrefixHelper.removePrefix(other.getValue());
		// TODO debug logging
		// System.out.println("Comparing values [" + valueA + "] and [" + valueA
		// + "]");
		if (!valueA.equals(valueB)) {
			// TODO debug logging
			// System.out.println("Values not identical");
			return true;
		}
		return false;
	}

	public Attribute getAttribute() {
		return attribute;
	}
}
