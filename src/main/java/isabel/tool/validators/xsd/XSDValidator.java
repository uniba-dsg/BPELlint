package isabel.tool.validators.xsd;

import isabel.tool.ValidationException;
import org.pmw.tinylog.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.SchemaFactory;
import java.nio.file.Path;

class XSDValidator {

    private static SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    public void validate(Path path) throws ValidationException {
        // Load the main schema
        try {
            sFactory.newSchema(path.toFile());
            Logger.debug("File " + path + " is a valid XSD file");
        } catch (SAXException e) {
            throw new ValidationException("File " + path + " is no XML Schema", e);
        }
    }
}
