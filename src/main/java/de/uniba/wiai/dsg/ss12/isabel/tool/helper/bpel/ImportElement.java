package de.uniba.wiai.dsg.ss12.isabel.tool.helper.bpel;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.Element;
import nu.xom.Node;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class ImportElement {

	private final Element element;

	public ImportElement(Element element) {
		Objects.requireNonNull(element, "<bpel:import> element must not be null");

		this.element = element;
	}

	public ImportElement(Node element) {
		this((Element) element);
	}

	public String getNamespace() {
		String namespace = element.getAttributeValue("namespace");
		// TODO solve  default value problem
		if(namespace == null){
			return "";
		} else {
			return namespace;
		}
	}

	public String getImportType() {
		return element.getAttributeValue("importType");
	}

	public String getLocation() {
		return element.getAttributeValue("location");
	}

	public String getAbsoluteLocation(String folder) {
		try {
			return Paths.get(new File(folder, getLocation()).getAbsolutePath()).toFile().getCanonicalPath();
		} catch (IOException e) {
			Logger.error(e);
			return "";
		}
	}

	public boolean isXsdImport() {
		return Standards.XSD_NAMESPACE.equals(getImportType());
	}

	public boolean isWsdlImport() {
		return Standards.WSDL_NAMESPACE.equals(getImportType());
	}
}
