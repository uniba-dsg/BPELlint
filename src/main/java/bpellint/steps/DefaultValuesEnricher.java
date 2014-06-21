package bpellint.steps;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DefaultValuesEnricher {

	public static final String BPEL_XSD_PATH = "src/main/resources/bpel2/ws-bpel_executable.xsd";

	public static void main(String[] args) throws SAXException,
			ParserConfigurationException, IOException, TransformerException {
		InputStream inputStream = Files.newInputStream(Paths.get(args[0]));
		String bpelWithDefaults = new DefaultValuesEnricher()
				.validateAndApplyDefaultValuesToBpelFile(inputStream);
		System.out.println(bpelWithDefaults);
	}

	public String validateAndApplyDefaultValuesToBpelFile(
			InputStream inputStream) throws SAXException,
			ParserConfigurationException, IOException, TransformerException {
		SchemaFactory sFactory = SchemaFactory
				.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		Schema schema = sFactory
				.newSchema(new Source[]{
						new StreamSource(getClass().getResourceAsStream(
								"/xsd/xml.xsd")),
						new StreamSource(getClass().getResourceAsStream(
								"/bpel2/ws-bpel_executable.xsd")),
						new StreamSource(getClass().getResourceAsStream(
								"/xsd/ws-addr.xsd")),
						new StreamSource(getClass().getResourceAsStream(
								"/xsd/XMLSchema.xsd")),
						new StreamSource(getClass().getResourceAsStream(
								"/bpel2/ws-bpel_serviceref.xsd"))});

		Validator validator = schema.newValidator();

		DocumentBuilderFactory domFactory = DocumentBuilderFactory
				.newInstance();
		domFactory.setNamespaceAware(true); // never forget this
		DocumentBuilder builder = domFactory.newDocumentBuilder();
		Document doc = builder.parse(inputStream);

		DOMSource source = new DOMSource(doc);
		DOMResult result = new DOMResult();

		validator.validate(source, result);
		Document augmented = (Document) result.getNode();

		return toString(augmented);
	}

	private String toString(Document doc) throws TransformerException {
		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();
		StringWriter stringWriter = new StringWriter();
		Result output = new StreamResult(stringWriter);
		Source input = new DOMSource(doc);
		transformer.transform(input, output);
		return stringWriter.toString();
	}

}
