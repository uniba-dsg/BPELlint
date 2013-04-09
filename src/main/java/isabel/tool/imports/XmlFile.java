package isabel.tool.imports;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

import nu.xom.Document;
import isabel.tool.impl.Standards;

public class XmlFile {

	private final Document document;

	public XmlFile(Document document) {
		this.document = Objects.requireNonNull(document,
				"document must not be null");
	}

	public String getFilePath() {
		// special case required when loading from a resource - baseURI is empty
		// string in that case
		if ("".equals(document.getBaseURI())) {
			return "";
		}

		try {
			// converting file path
			return new File(new URL(document.getBaseURI()).toURI().getPath())
					.getAbsolutePath();
		} catch (URISyntaxException | MalformedURLException e) {
			// should not happen
			throw new IllegalStateException("bad url found: "
					+ document.getBaseURI());
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
		XmlFile other = (XmlFile) obj;
		if (getFilePath() == null) {
			if (other.getFilePath() != null)
				return false;
		} else if (!getFilePath().equals(other.getFilePath()))
			return false;
		return true;
	}

	boolean isWsdl() {
		return getDocument().getRootElement().getNamespaceURI()
				.equals(Standards.WSDL_NAMESPACE);
	}

	boolean isXsd() {
		return getDocument().getRootElement().getNamespaceURI()
				.equals(Standards.XSD_NAMESPACE);
	}

	@Override
	public String toString() {
		String type = "XML";
		if (isWsdl()) {
			type = "WSDL";
		} else if (isXsd()) {
			type = "XSD";
		}
		return type + " at [" + getFilePath() + "] with targetNamespace ["
				+ getTargetNamespace() + "]";
	}
}
