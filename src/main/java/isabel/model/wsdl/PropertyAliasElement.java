package isabel.model.wsdl;

import isabel.tool.helper.NodeHelper;
import isabel.tool.helper.PrefixHelper;
import isabel.tool.impl.NavigationException;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.tool.impl.Standards.CONTEXT;

public class PropertyAliasElement extends NodeHelper {

    public PropertyAliasElement(Node node) {
        super(node);

        if (!getLocalName().equals("propertyAlias")) {
            throw new IllegalArgumentException(
                    "propertyAlias helper only works for wsdl:propertyAlias elements");
        }
    }

    public PropertyElement getCorrespondingProperty()
            throws NavigationException {
        Document wsdlDom = this.asElement().getDocument();
        String propertyAttribute = new NodeHelper(this.asElement())
                .getAttribute("propertyName");
        Nodes properties = wsdlDom.query("//vprop:property", CONTEXT);

        for (Node propertyNode : properties) {
            String propertyName = new NodeHelper(propertyNode).getAttribute("name");

            if (propertyName.equals(PrefixHelper
                    .removePrefix(propertyAttribute))) {
                return new PropertyElement(propertyNode);
            }
        }
        throw new NavigationException("Referenced <property> does not exist.");
    }

}
