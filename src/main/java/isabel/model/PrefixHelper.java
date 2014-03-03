package isabel.model;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;

public class PrefixHelper {

	public static String removePrefix(String localAttribute) {
		if (localAttribute.contains(":")) {
			return localAttribute.substring(localAttribute.indexOf(":") + 1);
		} else {
			return localAttribute;
		}
	}

	public static String getPrefix(String value) {
		String[] qName = value.split(":");
		if (qName.length > 1) {
			return qName[0];
		} else {
			return "";
		}
	}

	public static String getPrefixNamespaceURI(Node node, String namespacePrefix)
			throws NavigationException {
		if (namespacePrefix.isEmpty()) {
			throw new NavigationException("namespacePrefix is empty");
		}
		if (node instanceof Attribute) {
			Attribute attribute = (Attribute) node;
			return getPrefixNamespaceURI(attribute.getParent(), namespacePrefix);
		}
		if (node instanceof Element) {
			Element element = (Element) node;
			String namespaceURI = element.getNamespaceURI(namespacePrefix);
			if (namespaceURI == null) {
				if ("process".equals(element.getLocalName())) {
					throw new NavigationException("Document has no namespace for this prefix \""+namespacePrefix+"\"");
				} else {
					return getPrefixNamespaceURI(element.getParent(), namespacePrefix);
				}
			}
			return namespaceURI;
		}
		throw new NavigationException("node need to be instance of Element or Attribute");
	}

	public static String resolveQNameToNamespace(Node node, String namespacePrefix) throws NavigationException {
		return getPrefixNamespaceURI(node, getPrefix(namespacePrefix));
	}
}
