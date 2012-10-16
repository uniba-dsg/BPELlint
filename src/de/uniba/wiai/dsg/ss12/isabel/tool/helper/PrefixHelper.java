package de.uniba.wiai.dsg.ss12.isabel.tool.helper;

public class PrefixHelper {

	public static String removePrefix(String localAttribute) {
		if (localAttribute.contains(":")) {
			return localAttribute.substring(localAttribute
					.indexOf(":") + 1);
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
}
