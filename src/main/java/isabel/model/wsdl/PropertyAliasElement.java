package isabel.model.wsdl;

import isabel.model.NavigationException;
import isabel.model.NodeHelper;
import isabel.model.PrefixHelper;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class PropertyAliasElement extends NodeHelper {

    public PropertyAliasElement(Node node) {
        super(node);

        if (!getLocalName().equals("propertyAlias")) {
            throw new IllegalArgumentException(
                    "propertyAlias helper only works for wsdl:propertyAlias elements");
        }
    }

    public PropertyElement getProperty()
            throws NavigationException {
        Document wsdlDom = this.asElement().getDocument();
        Nodes properties = wsdlDom.query("//vprop:property", CONTEXT);

        for (Node propertyNode : properties) {
            String propertyName = new NodeHelper(propertyNode).getAttribute("name");

            if (propertyName.equals(PrefixHelper.removePrefix(getPropertyAttribute()))) {
                return new PropertyElement(propertyNode);
            }
        }
        throw new NavigationException("Referenced <property> does not exist.");
    }

    private String getPropertyAttribute() {
        return getAttribute("propertyName");
    }

}
