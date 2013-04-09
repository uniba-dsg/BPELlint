package isabel.tool.validators.xsd;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;

import org.pmw.tinylog.Logger;
import org.xml.sax.SAXException;

import isabel.tool.ValidationException;

class XSDValidator {

	private static SchemaFactory sFactory = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	public void validate(String file) throws ValidationException {
		// Load the main schema
		try {
			sFactory.newSchema(new File(file));
			Logger.debug("File " + file + " is a valid XSD file");
		} catch (SAXException e) {
			throw new ValidationException("File " + file + " is no XML Schema",
					e);
		}
	}
}
