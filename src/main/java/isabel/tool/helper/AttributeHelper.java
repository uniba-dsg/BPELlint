package isabel.tool.helper;

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

		return !hasDifferentAttributeNamespace(other) && !hasDifferentAttributeValue(other);

	}

	public boolean hasDifferentAttributeNamespace(Attribute other) {
		String namespaceA = ((Element) attribute.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(attribute.getValue()));
		String namespaceB = ((Element) other.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(other.getValue()));

		// TODO debug logging
		// System.out.println("Comparing namespaces [" + namespaceA + "] and ["
		// + namespaceB + "]");
		return !namespaceA.equals(namespaceB);
	}

	public boolean hasDifferentAttributeValue(Attribute other) {
		String valueA = PrefixHelper.removePrefix(attribute.getValue());
		String valueB = PrefixHelper.removePrefix(other.getValue());
		// TODO debug logging
		// System.out.println("Comparing values [" + valueA + "] and [" + valueA
		// + "]");
		return !valueA.equals(valueB);
	}

	public Attribute getAttribute() {
		return attribute;
	}
}
