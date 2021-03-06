package bpellint.core.model;

import static bpellint.core.model.Standards.CONTEXT;
import static bpellint.core.model.Standards.XSD_NAMESPACE;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import org.pmw.tinylog.Logger;

import bpellint.core.model.bpel.CorrelationElement;
import bpellint.core.model.bpel.CorrelationSetElement;
import bpellint.core.model.bpel.CorrelationSetsElement;
import bpellint.core.model.bpel.CorrelationsElement;
import bpellint.core.model.bpel.EventHandlersElement;
import bpellint.core.model.bpel.ImportElement;
import bpellint.core.model.bpel.PartnerLinkElement;
import bpellint.core.model.bpel.PartnerLinksElement;
import bpellint.core.model.bpel.PickElement;
import bpellint.core.model.bpel.ProcessElement;
import bpellint.core.model.bpel.ScopeElement;
import bpellint.core.model.bpel.fct.CatchAllElement;
import bpellint.core.model.bpel.fct.CatchElement;
import bpellint.core.model.bpel.fct.CompensateElement;
import bpellint.core.model.bpel.fct.CompensateScopeElement;
import bpellint.core.model.bpel.fct.CompensationHandlerElement;
import bpellint.core.model.bpel.fct.FaultHandlersElement;
import bpellint.core.model.bpel.fct.FctHandler;
import bpellint.core.model.bpel.fct.RethrowElement;
import bpellint.core.model.bpel.fct.TerminationHandlerElement;
import bpellint.core.model.bpel.flow.FlowElement;
import bpellint.core.model.bpel.flow.LinkElement;
import bpellint.core.model.bpel.flow.LinkEntity;
import bpellint.core.model.bpel.flow.LinksElement;
import bpellint.core.model.bpel.flow.SourceElement;
import bpellint.core.model.bpel.flow.SourcesElement;
import bpellint.core.model.bpel.flow.TargetElement;
import bpellint.core.model.bpel.flow.TargetsElement;
import bpellint.core.model.bpel.mex.InvokeElement;
import bpellint.core.model.bpel.mex.MessageActivity;
import bpellint.core.model.bpel.mex.OnEventElement;
import bpellint.core.model.bpel.mex.OnMessageElement;
import bpellint.core.model.bpel.mex.ReceiveElement;
import bpellint.core.model.bpel.mex.ReplyElement;
import bpellint.core.model.bpel.mex.StartActivity;
import bpellint.core.model.bpel.var.CopyEntity;
import bpellint.core.model.bpel.var.FromElement;
import bpellint.core.model.bpel.var.ToElement;
import bpellint.core.model.bpel.var.VariableElement;
import bpellint.core.model.bpel.var.VariablesElement;
import bpellint.core.model.wsdl.OperationElement;
import bpellint.core.model.wsdl.PortTypeElement;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;

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
			throw new IllegalArgumentException(
					"Expected WSDL or XSD file, got something else for "
							+ xmlFile);
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
			throw new IllegalArgumentException(
					"Expected WSDL or XSD file, got something else for "
							+ xmlFile);
		}
	}

	public void addSchema(Node schema) {
		schemas.add(Objects.requireNonNull(schema,
				"a schema reference is required"));
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

    public List<XmlFile> getWsdlsAndXsds() {
        List<XmlFile> result = new LinkedList<>();
        result.addAll(getWsdls());
        result.addAll(getXsds());
        return result;
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

		throw new NavigationException(
				"XMLSchema should have been imported, but haven't.");
	}

	public Nodes getCorrelationSets() {
		return getBpel().getDocument().query("//bpel:correlationSet", CONTEXT);
	}

	public ProcessElement getProcess() {
		return new ProcessElement(getBpel().getDocument()
				.query("/bpel:*", CONTEXT).get(0), this);
	}

	public List<OperationElement> getAllOperations() {
		List<OperationElement> result = new LinkedList<>();

		for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
			result.addAll(getOperations(wsdlEntry));
		}

		return result;
	}
    private List<OperationElement> getOperations(XmlFile wsdlEntry) {
        List<OperationElement> operations = new LinkedList<>();

        for (Node node : wsdlEntry.getDocument().query("//wsdl:portType/wsdl:operation", CONTEXT)) {
           operations.add(new OperationElement(node, this));
        }

        return operations;
    }

	public List<PortTypeElement> getAllPortTypes() {
		List<PortTypeElement> result = new LinkedList<>();

		for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
			result.addAll(getPortTypes(wsdlEntry));
		}

		return result;
	}

    private List<PortTypeElement> getPortTypes(XmlFile wsdlEntry) {
        List<PortTypeElement> operations = new LinkedList<>();

        for (Node node : wsdlEntry.getDocument().query("//wsdl:portType", CONTEXT)) {
           operations.add(new PortTypeElement(node, this));
        }

        return operations;
    }
    
	public List<PropertyElement> getAllProperties() {
		List<PropertyElement> result = new LinkedList<>();

		for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
			result.addAll(getProperties(wsdlEntry));
		}

		return result;
	}

    private List<PropertyElement> getProperties(XmlFile wsdlEntry) {
        List<PropertyElement> operations = new LinkedList<>();

        for (Node node : wsdlEntry.getDocument().query("//vprop:property", CONTEXT)) {
            operations.add(new PropertyElement(node, this));
        }

        return operations;
    }

	public List<PropertyAliasElement> getAllPropertyAliases() {
		List<PropertyAliasElement> result = new LinkedList<>();

		for (XmlFile wsdlEntry : getDirectlyImportedWsdls()) {
			result.addAll(getPropertyAliases(wsdlEntry));
		}

		return result;
	}

    public List<PropertyAliasElement> getPropertyAliases(XmlFile wsdlEntry) {
        List<PropertyAliasElement> operations = new LinkedList<>();

        for (Node node : wsdlEntry.getDocument().query("//vprop:propertyAlias", CONTEXT)) {
            operations.add(new PropertyAliasElement(node, this));
        }

        return operations;
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

		for (Node node : getBpel().getDocument().query("//bpel:rethrow",
				CONTEXT)) {
			result.add(new RethrowElement(node, this));
		}

		return result;
	}

	public List<CompensateScopeElement> getAllCompensateScopes() {
		List<CompensateScopeElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query(
				"//bpel:compensateScope", CONTEXT)) {
			result.add(new CompensateScopeElement(node, this));
		}

		return result;
	}

	public List<CorrelationElement> getAllCorrelations() {
		List<CorrelationElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:correlation",
				CONTEXT)) {
			result.add(new CorrelationElement(node, this));
		}

		return result;
	}

	public List<CorrelationsElement> getAllCorrelationContainer() {
		List<CorrelationsElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:correlations",
				CONTEXT)) {
			result.add(new CorrelationsElement(node, this));
		}

		return result;
	}

	public List<ScopeElement> getAllScopes() {
		List<ScopeElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:scope", CONTEXT)) {
			result.add(new ScopeElement(node, this));
		}

		return result;
	}

	public List<FaultHandlersElement> getAllFaultHandlerContainers() {
		List<FaultHandlersElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:faultHandlers",
				CONTEXT)) {
			result.add(new FaultHandlersElement(node, this));
		}

		return result;
	}

	public List<EventHandlersElement> getAllEventHandlers() {
		List<EventHandlersElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:eventHandlers",
				CONTEXT)) {
			result.add(new EventHandlersElement(node, this));
		}

		return result;
	}

	public List<CorrelationsElement> getAllCorrelationsWithinInvokes() {
		List<CorrelationsElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query(
				"//bpel:invoke/bpel:correlations", CONTEXT)) {
			result.add(new CorrelationsElement(node, this));
		}

		return result;
	}

	public List<CompensateElement> getAllCompensates() {
		List<CompensateElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:compensate",
				CONTEXT)) {
			result.add(new CompensateElement(node, this));
		}

		return result;
	}

	public List<PartnerLinkElement> getAllPartnerLinks() {
		List<PartnerLinkElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:partnerLink",
				CONTEXT)) {
			result.add(new PartnerLinkElement(node, this));
		}

		return result;
	}

	public List<PartnerLinksElement> getAllPartnerLinksContainer() {
		List<PartnerLinksElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:partnerLinks",
				CONTEXT)) {
			result.add(new PartnerLinksElement(node, this));
		}

		return result;
	}

	public List<VariableElement> getAllVariables() {
		List<VariableElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:variable",
				CONTEXT)) {
			result.add(new VariableElement(node, this));
		}

		return result;
	}

	public List<VariablesElement> getAllVariablesContainer() {
		List<VariablesElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:variables",
				CONTEXT)) {
			result.add(new VariablesElement(node, this));
		}

		return result;
	}

	public List<CorrelationSetsElement> getAllCorrelationSetsContainer() {
		List<CorrelationSetsElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query(
				"//bpel:correlationSets", CONTEXT)) {
			result.add(new CorrelationSetsElement(node, this));
		}

		return result;
	}

	public List<CorrelationSetElement> getAllCorrelationSets() {
		List<CorrelationSetElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:correlationSet",
				CONTEXT)) {
			result.add(new CorrelationSetElement(node, this));
		}

		return result;
	}

	public List<InvokeElement> getAllInvokes() {
		List<InvokeElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument()
				.query("//bpel:invoke", CONTEXT)) {
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

		for (Node node : getBpel().getDocument().query("//bpel:receive",
				CONTEXT)) {
			result.add(new ReceiveElement(node, this));
		}

		return result;
	}

	public List<OnMessageElement> getAllOnMessages() {
		List<OnMessageElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:onMessage",
				CONTEXT)) {
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

		for (Node node : getBpel().getDocument().query("//bpel:onEvent",
				CONTEXT)) {
			result.add(new OnEventElement(node, this));
		}

		return result;
	}

	public List<ToElement> getAllTos() {
		List<ToElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:to", CONTEXT)) {
			result.add(new ToElement(node, this));
		}

		return result;
	}

	public List<FromElement> getAllFroms() {
		List<FromElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:from", CONTEXT)) {
			result.add(new FromElement(node, this));
		}

		return result;
	}
	
	public List<CopyEntity> getAllCopyEntities() {
		List<CopyEntity> fromAndToElements = new LinkedList<>();
        fromAndToElements.addAll(getAllTos());
        fromAndToElements.addAll(getAllFroms());
        return fromAndToElements;
	}

	public List<SourceElement> getAllSources() {
		List<SourceElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument()
				.query("//bpel:source", CONTEXT)) {
			result.add(new SourceElement(node, this));
		}

		return result;
	}

	public List<SourcesElement> getAllSourcesContainer() {
		List<SourcesElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:sources",
				CONTEXT)) {
			result.add(new SourcesElement(node, this));
		}

		return result;
	}

	public List<TargetElement> getAllTargets() {
		List<TargetElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument()
				.query("//bpel:target", CONTEXT)) {
			result.add(new TargetElement(node, this));
		}

		return result;
	}

	public List<TargetsElement> getAllTargetsContainer() {
		List<TargetsElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:targets",
				CONTEXT)) {
			result.add(new TargetsElement(node, this));
		}

		return result;
	}

	public List<LinkElement> getAllLinks() {
		List<LinkElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:link", CONTEXT)) {
			result.add(new LinkElement(node, this));
		}

		return result;
	}

	public List<LinksElement> getAllLinksContainer() {
		List<LinksElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:links", CONTEXT)) {
			result.add(new LinksElement(node, this));
		}

		return result;
	}

	public List<LinkEntity> getAllLinkEntities() {
		List<LinkEntity> result = new LinkedList<>();
		result.addAll(getAllSources());
		result.addAll(getAllTargets());

		return result;
	}

	public List<PickElement> getAllPicks() {
		List<PickElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:pick", CONTEXT)) {
			result.add(new PickElement(node, this));
		}

		return result;
	}

	public List<StartActivity> getAllStartActivities() {
		List<StartActivity> possibleStarts = new LinkedList<>();
		possibleStarts.addAll(getAllReceives());
		possibleStarts.addAll(getAllOnMessages());

		List<StartActivity> startActivities = new LinkedList<>();
		for (StartActivity startActivity : possibleStarts) {
			if (startActivity.isStartActivity()) {
				startActivities.add(startActivity);
			}
		}
		return startActivities;
	}

	public List<TerminationHandlerElement> getAllTerminationHandler() {
		List<TerminationHandlerElement> result = new LinkedList<>();

		for (Node terminationHandler : getBpel().getDocument().query("//bpel:terminationHandler", CONTEXT)) {
			result.add(new TerminationHandlerElement(terminationHandler, this));
		}

		return result;
	}

	public List<CatchElement> getAllCatch() {
		List<CatchElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:catch", CONTEXT)) {
			result.add(new CatchElement(node, this));
		}

		return result;
	}

	public List<CatchAllElement> getAllCatchAll() {
		List<CatchAllElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query("//bpel:catchAll",
				CONTEXT)) {
			result.add(new CatchAllElement(node, this));
		}

		return result;
	}

	public List<CompensationHandlerElement> getAllCompensationHandler() {
		List<CompensationHandlerElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query(
				"//bpel:compensationHandler", CONTEXT)) {
			result.add(new CompensationHandlerElement(node, this));
		}

		return result;
	}

	public List<FctHandler> getAllFctHandler() {
		List<FctHandler> result = new LinkedList<>();
		result.addAll(getAllCatch());
		result.addAll(getAllCatchAll());
		result.addAll(getAllCompensationHandler());
		result.addAll(getAllTerminationHandler());

		return result;
	}

	public List<MessageActivity> getAllIncomingMessageActivities() {
		List<MessageActivity> result = new LinkedList<>();
		result.addAll(getAllInvokes());
		result.addAll(getAllReceives());
		result.addAll(getAllOnMessages());
		result.addAll(getAllOnEvents());

		return result;
	}

	public List<FlowElement> getAllFlows() {
		List<FlowElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument().query(
				"//bpel:flow", CONTEXT)) {
			result.add(new FlowElement(node, this));
		}

		return result;
	}

	void validate() {
		// assertion
		if (getWsdls().isEmpty()) {
			throw new IllegalStateException(
					"At least one WSDL file is required");
		}
	}

	@Override
	public String toString() {
		return new ProcessContainerPrinter(this).toString();
	}

	public List<ImportElement> getAllImports() {
		List<ImportElement> result = new LinkedList<>();

		for (Node node : getBpel().getDocument()
				.query("//bpel:import", CONTEXT)) {
			result.add(new ImportElement(node, this));
		}

		return result;
	}

	public Node resolveName(String targetNamespace, String name, String type) throws NavigationException {
		List<XmlFile> xmlFiles = new LinkedList<>();
		for (XmlFile xmlFile : getDirectlyImportedWsdls()) {
			xmlFiles.add(xmlFile);
		}
		for (XmlFile xmlFile : getDirectlyImportedXsds()) {
			xmlFiles.add(xmlFile);
		}
		for (XmlFile xml : xmlFiles) {
			if (!xml.getTargetNamespace().equals(targetNamespace)) {
				continue;
			}
			Nodes element = xml.getDocument().query("//" + type + "[@name='" + name + "']", CONTEXT);
			if (!element.hasAny()) {
				continue;
			}
			return element.get(0);
		}
		throw new NavigationException(name + " of type " + type + " is not defined in WSDL or XSD file");
	}
}
