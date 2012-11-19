package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.Document;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class DocumentEntry {

	private final Document document;

	public DocumentEntry(Document document) {
		this.document = Objects.requireNonNull(document, "document must not be null");
	}

	public String getFilePath() {
		// special case required when loading from a resource - baseURI is empty string in that case
		if("".equals(document.getBaseURI())){
			return "";
		}

		try {
			// converting file path
			return new File(new URL(document.getBaseURI()).toURI().getPath()).getAbsolutePath();
		} catch (URISyntaxException | MalformedURLException e) {
			// should not happen
			throw new IllegalStateException("bad url found: " + document.getBaseURI());
		}
	}

	public Document getDocument() {
		return document;
	}

	public String getNamespace() {
		return document.getRootElement().getNamespaceURI();
	}

	public String getTargetNamespace() {
		return document.getRootElement().getAttributeValue("targetNamespace");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((getFilePath() == null) ? 0 : getFilePath().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentEntry other = (DocumentEntry) obj;
		if (getFilePath() == null) {
			if (other.getFilePath() != null)
				return false;
		} else if (!getFilePath().equals(other.getFilePath()))
			return false;
		return true;
	}

	boolean isWsdl() {
		return getDocument().getRootElement().getNamespaceURI()
				.startsWith(Standards.WSDL_NAMESPACE);
	}

	boolean isXsd() {
		return getDocument().getRootElement().getNamespaceURI()
				.startsWith(Standards.XSD_NAMESPACE);
	}

	@Override
	public String toString() {
		if(isWsdl()){
			return "WSDL at " + getFilePath() + " with targetNamespace " + getTargetNamespace();
		} else if(isXsd()){
			return "XSD at " + getFilePath() + " with targetNamespace " + getTargetNamespace();
		} else {
			return "XML at " + getFilePath() + " with targetNamespace " + getTargetNamespace();
		}
	}
}
