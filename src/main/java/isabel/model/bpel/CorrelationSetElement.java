package isabel.model.bpel;

import isabel.model.ContainerAwareReferable;
import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import isabel.model.ProcessContainer;
import nu.xom.Node;

import java.util.ArrayList;
import java.util.List;

public class CorrelationSetElement extends ContainerAwareReferable {

    private final NodeHelper correlationSet;

	public CorrelationSetElement(Node correlationSet, ProcessContainer processContainer) {
        super(correlationSet, processContainer);
        this.correlationSet = new NodeHelper(correlationSet, "correlationSet");
    }

    public List<String> getPropertyIdentifiers() {
        List<String> result = new ArrayList<>();

        // is a List of QNames which can only be accessed as a String
        String properties = correlationSet.getAttribute("properties");
        for (String property : properties.split(" ")) {
            // resolving prefix via element
            String prefix = PrefixHelper.getPrefix(property);
            String namespace = correlationSet.asElement().getNamespaceURI(prefix);

            String propertyValueWithoutPrefix = PrefixHelper.removePrefix(property);

            result.add("{" + namespace + "}" + propertyValueWithoutPrefix);
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
