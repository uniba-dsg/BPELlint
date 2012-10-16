package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import nu.xom.Element;
import nu.xom.NodeFactory;
import org.xml.sax.Locator;

final class LocationAwareNodeFactory extends NodeFactory {

	@Override
	public Element startMakingElement(String arg0, String arg1, Locator locator) {
		Element element = super.startMakingElement(arg0, arg1, locator);

		element.setUserData("columnNumber", locator.getColumnNumber());
		element.setUserData("lineNumber", locator.getLineNumber());

		return element;
	}
}