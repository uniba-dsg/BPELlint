package isabel.model.bpel;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import nu.xom.Node;

import java.util.ArrayList;
import java.util.List;

public class CorrelationSetElement extends NodeHelper {

    public CorrelationSetElement(Node node) {
        super(node);
    }

    public List<String> getPropertyIdentifiers() {
        List<String> result = new ArrayList<>();

        // is a List of QNames which can only be accessed as a String
        String properties = this.getAttribute("properties");
        for (String property : properties.split(" ")) {
            // resolving prefix via element
            String prefix = PrefixHelper.getPrefix(property);
            String namespace = asElement().getNamespaceURI(prefix);

            String propertyValueWithoutPrefix = PrefixHelper.removePrefix(property);

            result.add("{" + namespace + "}" + propertyValueWithoutPrefix);
        }

        return result;
    }

    public String getName() {
        return getAttribute("name");
    }

    public String getPropertiesAttribute() {
        return getAttribute("properties");
    }

    public String getCorrelationPropertyAliasPrefix()
            throws NavigationException {

        String propertyAliasName = getPropertiesAttribute();
        String namespacePrefix = PrefixHelper.getPrefix(propertyAliasName);

        if (namespacePrefix != null) {
            return namespacePrefix;
        }

        throw new NavigationException("<correlationSet>@properties prefix does not exist.");
    }
}
