package isabel.model;

import nu.xom.Attribute;
import nu.xom.Element;

public class AttributeHelper {

	private final Attribute attribute;

	public AttributeHelper(Attribute attribute) {
		this.attribute = attribute;
	}

	public boolean isEqualTo(Attribute other) {
		return !hasDifferentAttributeNamespace(other) && !hasDifferentAttributeValue(other);

	}

	public boolean hasDifferentAttributeNamespace(Attribute other) {
		String namespaceA = ((Element) attribute.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(attribute.getValue()));
		String namespaceB = ((Element) other.getParent())
				.getNamespaceURI(PrefixHelper.getPrefix(other.getValue()));

		return !namespaceA.equals(namespaceB);
	}

	public boolean hasDifferentAttributeValue(Attribute other) {
		String valueA = PrefixHelper.removePrefix(attribute.getValue());
		String valueB = PrefixHelper.removePrefix(other.getValue());
		return !valueA.equals(valueB);
	}

	public Attribute getAttribute() {
		return attribute;
	}
}
