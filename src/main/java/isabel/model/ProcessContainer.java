package isabel.model;

import isabel.model.bpel.*;
import isabel.model.bpel.mex.*;
import isabel.model.wsdl.OperationElement;
import isabel.model.wsdl.PortTypeElement;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;
import isabel.tool.validators.rules.VariablesElement;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import org.pmw.tinylog.Logger;

import java.nio.file.Path;
import java.util.*;

import static isabel.model.Standards.CONTEXT;
import static isabel.model.Standards.XSD_NAMESPACE;

public class ProcessContainer {

    private XmlFile bpel;

    private List<XmlFile> allFiles = new ArrayList<>();

    private List<XmlFile> wsdls = new ArrayList<>();
    private List<XmlFile> xsds = new ArrayList<>();

    private List<XmlFile> directlyImportedWsdls = new ArrayList<>();
    private List<XmlFile> directlyImportedXsds = new ArrayList<>();

    private List<Node> schemas = new ArrayList<>();

    private void addWsdl(XmlFile xmlFile) {
        Objects.requireNonNull(xmlFile, "a xmlFile reference is required");
        xmlFile.failUnlessWsdl();
        wsdls.add(xmlFile);
        allFiles.add(xmlFile);
    }

    private void addXsd(XmlFile xmlFile) {
        Objects.requireNonNull(xmlFile, "a xmlFile reference is required");
        xmlFile.failUnlessXsd();
        xsds.add(xmlFile);
        allFiles.add(xmlFile);
    }

    public void add(XmlFile xmlFile) {
        if (xmlFile.isWsdl()) {
            addWsdl(xmlFile);
        } else if (xmlFile.isXsd()) {
            addXsd(xmlFile);
        } else {
            throw new IllegalArgumentException("Expected WSDL or XSD file, got something else for " + xmlFile);
        }
    }

    public void addDirectlyImported(XmlFile xmlFile) {
        if (xmlFile.isWsdl()) {
            addWsdl(xmlFile);
            directlyImportedWsdls.add(xmlFile);
        } else if (xmlFile.isXsd()) {
            addXsd(xmlFile);
            directlyImportedXsds.add(xmlFile);
        } else {
            throw new IllegalArgumentException("Expected WSDL or XSD file, got something else for " + xmlFile);
        }
    }

    public void addSchema(Node schema) {
        schemas.add(Objects.requireNonNull(schema, "a schema reference is required"));
    }

    public void setBpel(XmlFile xmlFile) {
        Objects.requireNonNull(xmlFile, "a bpel reference is required");
        xmlFile.failUnlessBpel();
        this.bpel = xmlFile;
        allFiles.add(xmlFile);
    }

    public boolean contains(XmlFile xmlFile) {
        return allFiles.contains(xmlFile);
    }

    public Path getAbsoluteBpelFolder() {
        return this.bpel.getFilePath().getParent();
    }

    public XmlFile getBpel() {
        return bpel;
    }

    public List<XmlFile> getWsdls() {
        return wsdls;
    }

    public List<XmlFile> getXsds() {
        return xsds;
    }

    public List<XmlFile> getDirectlyImportedWsdls() {
        return directlyImportedWsdls;
    }

    public List<XmlFile> getDirectlyImportedXsds() {
        return directlyImportedXsds;
    }

    public List<Node> getSchemas() {
        List<Node> xsdSchema = new ArrayList<>(schemas);
        for (XmlFile xsdNode : getXsds())
            xsdSchema.add(xsdNode.getDocument().getChild(0));

        return xsdSchema;
    }

    public Document getXmlSchema() throws NavigationException {
        for (XmlFile xmlFile : getXsds())
            if (XSD_NAMESPACE.equals(xmlFile.getTargetNamespace()))
                return xmlFile.getDocument();

        throw new NavigationException("XMLSchema should have been imported, but haven't.");
    }

    public XmlFile getWsdlByTargetNamespace(String searchedTargetNamespace)
            throws NavigationException {
        for (XmlFile wsdlEntry : getWsdls())
            if (wsdlEntry.getTargetNamespace().equals(searchedTargetNamespace))
                return wsdlEntry;

        throw new NavigationException("Document does not exist");
    }

    public Nodes getCorrelationSets() {
        return getBpel().getDocument().query("//bpel:correlationSet", CONTEXT);
    }

    public ProcessElement getProcess() {
        return new ProcessElement(getBpel().getDocument().query("/bpel:*", CONTEXT).get(0));
    }

    public List<OperationElement> getAllOperations() {
        List<OperationElement> result = new LinkedList<>();

        for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
            result.addAll(wsdlEntry.getOperations());
        }

        return result;
    }

    public List<PortTypeElement> getAllPortTypes() {
        List<PortTypeElement> result = new LinkedList<>();

        for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
            result.addAll(wsdlEntry.getPortTypes());
        }

        return result;
    }

    public List<PropertyElement> getAllProperties() {
        List<PropertyElement> result = new LinkedList<>();

        for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
            result.addAll(wsdlEntry.getProperties());
        }

        return result;
    }

    public List<PropertyAliasElement> getAllPropertyAliases() {
        List<PropertyAliasElement> result = new LinkedList<>();

        for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
            result.addAll(wsdlEntry.getPropertyAliases());
        }

        return result;
    }

    public ProcessContainer validateAndFinalize() {
        Logger.info("Creating immutable ProcessContainer {0}", this);

        validate();

        wsdls = Collections.unmodifiableList(wsdls);
        xsds = Collections.unmodifiableList(xsds);
        schemas = Collections.unmodifiableList(schemas);

        return this;
    }

    public List<RethrowElement> getAllRethrows() {
        List<RethrowElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:rethrow", CONTEXT)) {
            result.add(new RethrowElement(node));
        }

        return result;
    }

    public List<CompensateScopeElement> getAllCompensateScopes() {
        List<CompensateScopeElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:compensateScope", CONTEXT)) {
            result.add(new CompensateScopeElement(node));
        }

        return result;
    }

    public List<CorrelationsElement> getAllCorrelations() {
        List<CorrelationsElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:correlations", CONTEXT)) {
            result.add(new CorrelationsElement(node));
        }

        return result;
    }

    public List<ScopeElement> getAllScopes() {
        List<ScopeElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:scope", CONTEXT)) {
            result.add(new ScopeElement(node));
        }

        return result;
    }

    public List<CorrelationsElement> getAllCorrelationsWithinInvokes() {
        List<CorrelationsElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:invoke/bpel:correlations", CONTEXT)) {
            result.add(new CorrelationsElement(node));
        }

        return result;
    }

    public List<CompensateElement> getAllCompensates() {
        List<CompensateElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:compensate", CONTEXT)) {
            result.add(new CompensateElement(node));
        }

        return result;
    }

    public List<PartnerLinkElement> getAllPartnerLinks() {
        List<PartnerLinkElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:partnerLink", CONTEXT)) {
            result.add(new PartnerLinkElement(node));
        }

        return result;
    }

    public List<PartnerLinksElement> getAllPartnerLinksContainer() {
        List<PartnerLinksElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:partnerLinks", CONTEXT)) {
            result.add(new PartnerLinksElement(node));
        }

        return result;
    }

    public List<VariableElement> getAllVariables() {
        List<VariableElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:variable", CONTEXT)) {
            result.add(new VariableElement(node));
        }

        return result;
    }

    public List<VariablesElement> getAllVariablesContainer() {
        List<VariablesElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:variables", CONTEXT)) {
            result.add(new VariablesElement(node));
        }

        return result;
    }

    public List<CorrelationSetsElement> getAllCorrelationSetsContainer() {
        List<CorrelationSetsElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:correlationSets", CONTEXT)) {
            result.add(new CorrelationSetsElement(node));
        }

        return result;
    }

    public List<CorrelationSetElement> getAllCorrelationSets() {
        List<CorrelationSetElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:correlationSet", CONTEXT)) {
            result.add(new CorrelationSetElement(node));
        }

        return result;
    }

    public List<InvokeElement> getAllInvokes() {
        List<InvokeElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:invoke", CONTEXT)) {
            result.add(new InvokeElement(node, this));
        }

        return result;
    }

    public List<ReplyElement> getAllReplies() {
        List<ReplyElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:reply", CONTEXT)) {
            result.add(new ReplyElement(node, this));
        }

        return result;
    }

    public List<ReceiveElement> getAllReceives() {
        List<ReceiveElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:receive", CONTEXT)) {
            result.add(new ReceiveElement(node, this));
        }

        return result;
    }

    public List<OnMessageElement> getAllOnMessages() {
        List<OnMessageElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:onMessage", CONTEXT)) {
            result.add(new OnMessageElement(node, this));
        }

        return result;
    }

    public List<MessageActivity> getMessageActivities() {
        List<MessageActivity> messageActivities = new ArrayList<>();
        messageActivities.addAll(getAllInvokes());
        messageActivities.addAll(getAllReplies());
        messageActivities.addAll(getAllReceives());
        messageActivities.addAll(getAllOnMessages());
        messageActivities.addAll(getAllOnEvents());
        return messageActivities;
    }

    public List<OnEventElement> getAllOnEvents() {
        List<OnEventElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:onEvent", CONTEXT)) {
            result.add(new OnEventElement(node, this));
        }

        return result;
    }

    public List<ToElement> getAllTos() {
        List<ToElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:to", CONTEXT)) {
            result.add(new ToElement(node));
        }

        return result;
    }

    public List<FromElement> getAllFroms() {
        List<FromElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:from", CONTEXT)) {
            result.add(new FromElement(node));
        }

        return result;
    }


    void validate() {
        // assertion
        if (getWsdls().isEmpty()) {
            throw new IllegalStateException("At least one WSDL file is required");
        }
    }

    public String toString() {
        return new ProcessContainerPrinter(this).toString();
    }

    public List<ImportElement> getAllImports() {
        List<ImportElement> result = new LinkedList<>();

        for (Node node : getBpel().getDocument().query("//bpel:import", CONTEXT)) {
            result.add(new ImportElement(node));
        }

        return result;
    }
}
