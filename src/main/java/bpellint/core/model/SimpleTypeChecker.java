package bpellint.core.model;

import static bpellint.core.model.Standards.CONTEXT;
import static bpellint.core.model.Standards.XSD_NAMESPACE;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

public class SimpleTypeChecker {

	private ProcessContainer processContainer;
	private Referable referableFromWsdl;

	public SimpleTypeChecker(ProcessContainer processContainer, Referable referableFromWsdl) {
		super();
		this.processContainer = processContainer;
		this.referableFromWsdl = referableFromWsdl;
	}

	public boolean isSimpleType(String propertyType)
			throws NavigationException {
		String namespacePrefix = PrefixHelper.getPrefix(propertyType);
        String propertyTypeTargetNamespace = getImportNamespace(referableFromWsdl, namespacePrefix);

        if (XSD_NAMESPACE.equals(propertyTypeTargetNamespace)) {
            Document xmlSchema = processContainer.getXmlSchema();
            Nodes simpleTypes = xmlSchema.query("//xsd:simpleType", CONTEXT);
            for (Node simpleType : simpleTypes) {
                String simpleTypeName = new NodeHelper(simpleType).getAttribute("name");

                if (PrefixHelper.removePrefix(propertyType).equals(
                        simpleTypeName)) {
                    return true;
                }
            }
        }
        return false;
	}

    public static String getImportNamespace(Referable node, String namespacePrefix) {
        Element rootElement = node.toXOM().getDocument().getRootElement();

        try {
            return rootElement.getNamespaceURI(namespacePrefix);
        } catch (NullPointerException e) {
            /*
             * if the prefix is undefined in the root element, getNamespaceURI
			 * will throw a nullPointerException.
			 */
            return "";
        }
    }
}
