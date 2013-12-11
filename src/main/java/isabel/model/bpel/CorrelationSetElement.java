package isabel.model.bpel;

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
}
