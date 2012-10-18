package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import nu.xom.*;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class XmlFileLoader {

    public static final String XMLSCHEMA_XSD = "/XMLSchema.xsd";

    private final List<DocumentEntry> wsdlList = new ArrayList<>();
    private final List<DocumentEntry> xsdList = new ArrayList<>();
    private final Builder builder = new Builder(new LocationAwareNodeFactory());
    private final List<Node> xsdSchemaList = new ArrayList<>();

    public BpelProcessFiles loadAllProcessFiles(String bpelFilePath)
            throws ValidationException {
        if (bpelFilePath == null) {
            throw new ValidationException(new IllegalArgumentException(
                    "parameter bpelFilePath must not be null"));
        }

        try {
            return loadAllProcessFilesWithoutExceptions(bpelFilePath);
        } catch (ValidityException e) {
            throw new ValidationException(e,
                    "Loading failed: Not a valid BPEL-File");
        } catch (ParsingException e) {
            throw new ValidationException(e, "Loading failed: Not Parsable");
        } catch (FileNotFoundException e) {
            throw new ValidationException(e,
                    "Loading failed: File was not found");
        } catch (IOException e) {
            throw new ValidationException(e,
                    "Loading failed: File-Reading Error");
        }

    }

    private BpelProcessFiles loadAllProcessFilesWithoutExceptions(String bpelFilePath) throws ParsingException, IOException, ValidationException {
        DocumentEntry bpel = createBpelDocumentEntry(bpelFilePath);

        InputStream stream = XmlFileLoader.class.getResourceAsStream(XMLSCHEMA_XSD);
        if (stream == null) {
            throw new ValidationException("Could not load" + XMLSCHEMA_XSD);
        }
        try (InputStreamReader schemaFile = new InputStreamReader(stream)) {
            Document xmlSchemaDom = builder.build(schemaFile);
            DocumentEntry xmlSchemaEntry = new DocumentEntry(XMLSCHEMA_XSD,
                    Standards.XSD_NAMESPACE, xmlSchemaDom);
            xsdList.add(xmlSchemaEntry);

            Nodes imports = getImportLocations(bpel.getDocument());
            loadDirectImports(imports);
        }

        return new BpelProcessFiles(bpel, wsdlList, xsdList, xsdSchemaList,
                getAbsolutePath(bpelFilePath));
    }

    private String getAbsolutePath(String bpelFilePath) {
        return Paths.get(bpelFilePath).getParent().toAbsolutePath().toString();
    }

    private DocumentEntry createBpelDocumentEntry(String bpelFilePath) throws ParsingException, IOException {
        Document bpelDom = builder.build(new File(bpelFilePath));
        String qName = new NodeHelper(bpelDom).getTargetNamespace();
        return new DocumentEntry(bpelFilePath, qName, bpelDom);
    }

    private void loadDirectImports(Nodes imports) throws ParsingException,
            IOException {
        for (Node importNode : imports) {
            DocumentEntry entry = createImportDocumentEntry(importNode);
            if (isWsdl(entry)) {
                if (!wsdlList.contains(entry)) {
                    wsdlList.add(entry);
                    loadDirectImports(getImportLocations(entry.getDocument()));
                    addWsdlXsd(entry);
                }
            } else if (isXsd(entry)) {
                if (!xsdList.contains(entry)) {
                    xsdList.add(entry);
                    loadDirectImports(getImportLocations(entry.getDocument()));
                }
            }
        }
    }

    private void addWsdlXsd(DocumentEntry entry) throws
            ParsingException, IOException {
        Nodes typesNodes = entry.getDocument().query("//wsdl:types/*", CONTEXT);

        if (typesNodes.size() == 0) {
            return;
        }

        Node schemaNode = typesNodes.get(0);
        if (isXsdNode(schemaNode)
                && new NodeHelper(schemaNode).hasLocalName("schema")) {
            xsdSchemaList.add(schemaNode);
            addXsdImports(schemaNode);
        }
    }

    private void addXsdImports(Node schemaNode) throws
            ParsingException, IOException {
        DocumentEntry xsdEntry;
        Nodes schemaChildren = schemaNode.query("child::*", CONTEXT);
        for (Node node : schemaChildren) {
            if (isXsdNode(node)
                    && new NodeHelper(schemaNode).hasLocalName("import")) {
                xsdEntry = createImportDocumentEntry(node);
                xsdList.add(xsdEntry);
            }
        }
    }

    private String getNodeDirectory(Node node) {
        if (node.getBaseURI().isEmpty()) {
            return null;
        }

        return Paths.get(URI.create(node.getBaseURI())).getParent().toString();
    }

    private boolean isXsdNode(Node node) {
        return ((Element) node).getNamespaceURI().equals(
                Standards.XSD_NAMESPACE);
    }

    private Nodes getImportLocations(Document entry) {
        return entry.query("/*/*[name()=\"import\"]");
    }

    private boolean isXsd(DocumentEntry entry) {
        return entry.getDocument().getRootElement().getNamespaceURI()
                .startsWith(Standards.XSD_NAMESPACE);
    }

    private boolean isWsdl(DocumentEntry entry) {
        return entry.getDocument().getRootElement().getNamespaceURI()
                .startsWith(Standards.WSDL_NAMESPACE);
    }

    private DocumentEntry createImportDocumentEntry(Node importNode)
            throws ParsingException, IOException {
        String locationPath = Paths.get(getNodeDirectory(importNode),
                getImportPath(importNode)).toString();
        File importFile = new File(locationPath);
        Document importFileDom = builder.build(importFile);
        String targetNamespace = new NodeHelper(importFileDom).getTargetNamespace();
        return new DocumentEntry(importFile.getAbsolutePath(), targetNamespace,
                importFileDom);
    }

    private String getImportPath(Node node) {
        NodeHelper nodeHelper = new NodeHelper(node);

        if (nodeHelper.hasAttribute("schemaLocation")) {
            return Paths.get(nodeHelper.getAttribute("schemaLocation")).toString();
        } else if (nodeHelper.hasAttribute("location")) {
            return Paths.get(nodeHelper.getAttribute("location")).toString();
        }

        return null;
    }
}