package isabel.imports;

import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.XmlFile;
import nu.xom.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static isabel.model.Standards.CONTEXT;

public class ProcessContainerLoader {

    public static final String XMLSCHEMA_XSD = "/xsd/XMLSchema.xsd";
    private final XmlFileLoader xmlFileLoader = new XmlFileLoader();

    private ProcessContainer result;

    public ProcessContainer load(Path bpelFilePath) throws ImportException {
        try {
            return loadAllProcessFilesWithoutExceptions(bpelFilePath);
        } catch (ValidityException e) {
            throw new ImportException("Loading failed: Not a valid BPEL-File", e);
        } catch (ParsingException e) {
            throw new ImportException("Loading failed: Not Parsable", e);
        } catch (FileNotFoundException e) {
            throw new ImportException("Loading failed: File was not found", e);
        } catch (IOException e) {
            throw new ImportException("Loading failed: File-Reading Error", e);
        }
    }

    private ProcessContainer loadAllProcessFilesWithoutExceptions(Path bpelFilePath)
            throws ParsingException, IOException, ImportException {
        result = new ProcessContainer();

        result.setBpel(xmlFileLoader.load(bpelFilePath));
        result.add(xmlFileLoader.loadFromResourceStream(XMLSCHEMA_XSD));
        loadBpelImports();

        return result.validateAndFinalize();
    }

    private void loadBpelImports() throws ParsingException, IOException {
        for (Node importNode : result.getImports()) {
            XmlFile entry = xmlFileLoader.loadImportNode(importNode);
            this.result.addDirectlyImported(entry);
            loadImports(entry);
        }
    }

    private void loadImports(XmlFile entry) throws ParsingException, IOException {
        if (entry.isWsdl()) {
            loadWsdlImports(entry);
            loadXsdImportsInWsdl(entry);
        } else if (entry.isXsd()) {
            loadXsdImports(entry);
        }
    }

    private void loadXsdImports(XmlFile entry) throws ParsingException, IOException {
        for (Node importNode : entry.getDocument().query("//xsd:import", CONTEXT)) {
            XmlFile xmlFile = xmlFileLoader.loadImportNode(importNode);
            if (!result.contains(xmlFile)) {
                result.add(xmlFile);
                loadImports(xmlFile);
            }
        }
    }

    private void loadXsdImportsInWsdl(XmlFile entry) throws ParsingException, IOException {
        Nodes typesNodes = entry.getDocument().query("//wsdl:types/*", CONTEXT);

        if (typesNodes.isEmpty()) {
            return;
        }

        Node schemaNode = typesNodes.get(0);
        if (isXsdNode(schemaNode) && new NodeHelper(schemaNode).hasLocalName("schema")) {
            result.addSchema(schemaNode);
            addXsdImports(schemaNode);
        }
    }

    private void loadWsdlImports(XmlFile entry) throws ParsingException, IOException {
        for (Node importNode : entry.getDocument().query("//wsdl:import", CONTEXT)) {
            XmlFile xmlFile = xmlFileLoader.loadImportNode(importNode);
            if (!result.contains(xmlFile)) {
                result.add(xmlFile);
                loadImports(xmlFile);
            }
        }
    }

    private void addXsdImports(Node schemaNode) throws ParsingException, IOException {
        Nodes schemaChildren = schemaNode.query("child::*", CONTEXT);
        for (Node node : schemaChildren) {
            if (isXsdNode(node) && new NodeHelper(schemaNode).hasLocalName("import")) {
                result.add(xmlFileLoader.loadImportNode(node));
            }
        }
    }

    private boolean isXsdNode(Node node) {
        return ((Element) node).getNamespaceURI().equals(Standards.XSD_NAMESPACE);
    }


}