package de.uniba.wiai.dsg.ss12.isabel.tool.validators.xsd;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.pmw.tinylog.Logger;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;

public class XMLValidator {

	private static DocumentBuilderFactory dbf = DocumentBuilderFactory
			.newInstance();

	public void validate(String file) throws ValidationException {
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			db.parse(new File(file));
			Logger.debug("File " + file + " is a valid XML file");
		} catch (Exception e) {
			throw new ValidationException("The file " + file
					+ " is not well formed", e);
		}
	}

}