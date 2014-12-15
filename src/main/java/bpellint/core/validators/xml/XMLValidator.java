package bpellint.core.validators.xml;


import org.pmw.tinylog.Logger;

import api.ValidationException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Path;

public class XMLValidator {

    private static DocumentBuilderFactory dbf = DocumentBuilderFactory
            .newInstance();

    public void validate(Path path) throws ValidationException {
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.parse(path.toFile());
            Logger.debug("File " + path + " is a valid XML file");
        } catch (Exception e) {
            throw new ValidationException("The file " + path + " is not well formed", e);
        }
    }

}