package isabel.model.bpel;

import isabel.tool.impl.Standards;
import nu.xom.Element;
import nu.xom.Node;

import java.nio.file.Path;
import java.util.Objects;

public class ImportElement {

    private final Element element;

    public ImportElement(Element element) {
        Objects.requireNonNull(element,
                "<bpel:import> element must not be null");

        this.element = element;
    }

    public ImportElement(Node element) {
        this((Element) element);
    }

    public String getNamespace() {
        String namespace = element.getAttributeValue("namespace");
        // TODO solve default value problem
        if (namespace == null) {
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

    public Path getAbsoluteLocation(Path folder) {
        return folder.resolve(getLocation()).normalize().toAbsolutePath();
    }

    public boolean isXsdImport() {
        return Standards.XSD_NAMESPACE.equals(getImportType());
    }

    public boolean isWsdlImport() {
        return Standards.WSDL_NAMESPACE.equals(getImportType());
    }
}
