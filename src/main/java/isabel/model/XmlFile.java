package isabel.model;

import nu.xom.Document;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class XmlFile {

    private final Document document;

    public XmlFile(Document document) {
        this.document = Objects.requireNonNull(document,
                "document must not be null");
    }

    public Path getFilePath() {
        // special case required when loading from a resource - baseURI is empty
        // string in that case
        if ("".equals(document.getBaseURI())) {
            return Paths.get("");
        }

        try {
            // converting file path
            return Paths.get(new URL(document.getBaseURI()).toURI());
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

    public boolean isWsdl() {
        return getDocument().getRootElement().getNamespaceURI()
                .equals(Standards.WSDL_NAMESPACE);
    }

    public void failUnlessWsdl() {
        if (!isWsdl()) {
            throw new IllegalArgumentException("file " + getFilePath() + " is no WSDL file!");
        }
    }

    public void failUnlessXsd() {
        if (!isXsd()) {
            throw new IllegalArgumentException("file " + getFilePath() + " is no XSD file!");
        }
    }

    public void failUnlessBpel() {
    	// FIXME this may be optional
//        if (!isBpel()) {
//            throw new IllegalArgumentException("file " + getFilePath() + " is no BPEL file!");
//        }
    }

    public boolean isXsd() {
        return getDocument().getRootElement().getNamespaceURI()
                .equals(Standards.XSD_NAMESPACE);
    }

    public boolean isBpel() {
        return getDocument().getRootElement().getNamespaceURI()
                .equals(Standards.BPEL_NAMESPACE);
    }

    private String getType() {
        String type = "XML";
        if (isWsdl()) {
            type = "WSDL";
        } else if (isXsd()) {
            type = "XSD";
        } else if (isBpel()) {
            type = "BPEL";
        }
        return type;
    }

    @Override
    public String toString() {
        return getType() + " at [" + getFilePath() + "] with targetNamespace ["
                + getTargetNamespace() + "]";
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

}
