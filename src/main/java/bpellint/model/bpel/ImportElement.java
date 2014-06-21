package bpellint.model.bpel;

import nu.xom.Node;

import java.nio.file.Path;

import bpellint.model.ContainerAwareReferable;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;

public class ImportElement extends ContainerAwareReferable {

    private final NodeHelper importElement;

    public ImportElement(Node importNode, ProcessContainer processContainer) {
    	super(importNode, processContainer);
        this.importElement = new NodeHelper(importNode, "import");
    }

    public boolean hasNamespaceAttribute() {
        return importElement.hasAttribute("namespace");
    }

    public String getNamespace() {
        return importElement.getAttribute("namespace");
    }

    public String getImportType() {
        return importElement.getAttribute("importType");
    }

    public String getLocation() {
        return importElement.getAttribute("location");
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
