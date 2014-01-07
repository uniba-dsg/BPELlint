package isabel.model;

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

    public static String getPrefixNamespaceURI(Node node, String namespacePrefix) throws NavigationException {
        if (!(node instanceof Element)) {
			throw new NavigationException("A element is required as node.");
		}
        Element element = (Element) node;
    	if (namespacePrefix.isEmpty()) {
            throw new NavigationException("namespacePrefix is empty");
        }

        String namespaceURI = element.getNamespaceURI(namespacePrefix);
        if (namespaceURI == null) {
			if ("process".equals(element.getLocalName())) {
				throw new NavigationException("Document has no namespace for this prefix");
			} else {
				return getPrefixNamespaceURI(element.getParent(), namespacePrefix);
			}
		}
		return namespaceURI;
    }
}
