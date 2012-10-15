package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class XmlSchemaValidatorErrorHandler implements ErrorHandler {

	@Override
	public void error(SAXParseException ex) throws SAXException {
		ex.printStackTrace();
	}

	@Override
	public void fatalError(SAXParseException ex) throws SAXException {
		throw ex;
	}

	@Override
	public void warning(SAXParseException ex) throws SAXException {
		ex.printStackTrace();
	}

}
