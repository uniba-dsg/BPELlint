package bpellint.core.model.bpel;

import nu.xom.Node;

import java.util.LinkedList;
import java.util.List;

import bpellint.core.model.ContainerAwareReferable;
import bpellint.core.model.NavigationException;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.PrefixHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.wsdl.PropertyElement;

public class CorrelationSetElement extends ContainerAwareReferable {

    private final NodeHelper correlationSet;

	public CorrelationSetElement(Node correlationSet, ProcessContainer processContainer) {
        super(correlationSet, processContainer);
        this.correlationSet = new NodeHelper(correlationSet, "correlationSet");
    }

	public List<PropertyElement> getProperties() throws NavigationException {
        List<PropertyElement> result = new LinkedList<>();

        // is a List of QNames which can only be accessed as a String
        String properties = correlationSet.getAttribute("properties");
        for (String property : properties.split(" ")) {
            String targetNamespace = PrefixHelper.resolveQNameToNamespace(correlationSet.toXOM(), property);

            String name = PrefixHelper.removePrefix(property);
            Node propertyNode = getProcessContainer().resolveName(targetNamespace, name, "vprop:property");
            result.add(new PropertyElement(propertyNode, getProcessContainer()));
        }

        return result;
    }

    public String getName() {
        return correlationSet.getAttribute("name");
    }

    public String getPropertiesAttribute() {
        return correlationSet.getAttribute("properties");
    }

    public String getCorrelationPropertyAliasPrefix() throws NavigationException {
        String propertyAliasName = getPropertiesAttribute();
        String namespacePrefix = PrefixHelper.getPrefix(propertyAliasName);
        if (namespacePrefix == null) {
        	throw new NavigationException("<correlationSet>@properties prefix does not exist.");
        }

        return namespacePrefix;
    }
}
