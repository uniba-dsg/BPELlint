package isabel.tool.validators.xsd;

import isabel.tool.validators.ValidationException;

import org.pmw.tinylog.Logger;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.nio.file.Path;

class BPELValidator {

    private static final SchemaFactory sFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

    private final Validator validator;

    public BPELValidator() throws ValidationException {
        try {
            Logger.debug("Loading BPEL schemas");
            Schema schema = sFactory.newSchema(new Source[]{
                    new StreamSource(getClass().getResourceAsStream("/xsd/xml.xsd")),
                    new StreamSource(getClass().getResourceAsStream("/bpel2/ws-bpel_executable.xsd")),
                    new StreamSource(getClass().getResourceAsStream("/xsd/ws-addr.xsd")),
                    new StreamSource(getClass().getResourceAsStream("/xsd/XMLSchema.xsd")),
                    new StreamSource(getClass().getResourceAsStream("/bpel2/ws-bpel_serviceref.xsd"))});
            validator = schema.newValidator();
            Logger.debug("Loading BPEL schemas DONE");
        } catch (SAXException e) {
            throw new ValidationException("Schemas for BPEL Validation could not be found", e);
        }
    }

    public void validate(Path path) throws ValidationException {
        try {
            validator.validate(new StreamSource(path.toFile()));
            Logger.debug("File " + path + " is a valid BPEL file");
        } catch (SAXException | IOException e) {
            throw new ValidationException("BPEL file " + path + " is no valid BPEL file", e);
        }
    }

}
