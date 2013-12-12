package isabel.model;

import nu.xom.Document;

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

    public static String getPrefixNamespaceURI(Document document, String namespacePrefix) throws NavigationException {
        if (namespacePrefix.isEmpty()) {
            throw new NavigationException(
                    "Document has no namespace for this prefix");
        }

        return document.getRootElement().getNamespaceURI(namespacePrefix);
    }
}
