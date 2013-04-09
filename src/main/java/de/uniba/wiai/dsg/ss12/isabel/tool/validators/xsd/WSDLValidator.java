package de.uniba.wiai.dsg.ss12.isabel.tool.validators.xsd;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.pmw.tinylog.Logger;
import org.xml.sax.SAXException;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;

class WSDLValidator {

	private static SchemaFactory sFactory = SchemaFactory
			.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	private final Validator validator;

	public WSDLValidator() throws ValidationException {
		try {
			Schema schema = sFactory.newSchema(new Source[] {
					new StreamSource(getClass().getResourceAsStream(
							"/wsdl/wsdl.xsd")),
					new StreamSource(getClass().getResourceAsStream(
							"/wsdl/soap.xsd")),
					new StreamSource(getClass().getResourceAsStream(
							"/xsd/xml.xsd")),
					new StreamSource(getClass().getResourceAsStream(
							"/xsd/XMLSchema.xsd")),
					new StreamSource(getClass().getResourceAsStream(
							"/bpel2/ws-bpel_varprop.xsd")),
					new StreamSource(getClass().getResourceAsStream(
							"/bpel2/ws-bpel_plnktype.xsd")) });
			validator = schema.newValidator();
		} catch (SAXException e) {
			throw new ValidationException(
					"Schemas for WSDL Validation could not be found", e);
		}
	}

	public void validate(String file) throws ValidationException {
		try {
			validator.validate(new StreamSource(new File(file)));
			Logger.debug("File " + file + " is a valid WSDL file");
		} catch (SAXException | IOException e) {
			throw new ValidationException("WSDL file " + file
					+ " is no valid WSDL file", e);
		}
	}

}
