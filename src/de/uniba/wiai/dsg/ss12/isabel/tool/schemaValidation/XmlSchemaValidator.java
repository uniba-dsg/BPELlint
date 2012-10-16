package de.uniba.wiai.dsg.ss12.isabel.tool.schemaValidation;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;


public class XmlSchemaValidator {
	
	public static final String XSI_NAMESPACE_URI = XMLConstants.W3C_XML_SCHEMA_NS_URI;
	private Validator validator = null;

	public XmlSchemaValidator(URL schemaLocation){
		
		SchemaFactory factory = 
	            SchemaFactory.newInstance(XSI_NAMESPACE_URI);
		Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
			initialize(schema);
		} catch (SAXException e) {
			e.printStackTrace();
		}

	}
	
	public XmlSchemaValidator(File schemaLocation){	
	
		SchemaFactory factory = 
	            SchemaFactory.newInstance(XSI_NAMESPACE_URI);
		Schema schema;
		try {
			schema = factory.newSchema(schemaLocation);
			initialize(schema);
		} catch (SAXException e) {
			e.printStackTrace();
		}
	}
	
	
	public void initialize(Schema schema){	  
		validator = schema.newValidator();
	}
	

	public void validate(List<File> files){
		for(File file : files){
			validate(file);
		}
	  } 
	
	
	public void validate(File file){
       try {
			validator.validate(new StreamSource(file));
			System.out.println(file.getCanonicalPath() + " is valid.");
        } 
        catch (SAXException | IOException ex) {
            System.out.println(file.getAbsolutePath() + " is not valid: \n\t because " + ex.getMessage() + "\n");
        } 
	}

}
