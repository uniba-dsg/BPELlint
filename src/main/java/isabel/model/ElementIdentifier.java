package isabel.model;

import nu.xom.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * Identifies an node using {targetNamespace}@name.
 *
 * targetNamespace is an attribute from the root node and @name is a NCName of the current node.
 */
public class ElementIdentifier {

	private final Node node;

	public ElementIdentifier(Node node) {
		this.node = node;
	}

	/**
	 * The identifier {targetNamespace}@name which can be used for comparison of elements.
	 *
	 * @return {targetNamespace}@name
	 */
	public String toIdentifier() {
		String targetNamespace = new NodeHelper(node).getTargetNamespace();
		String name = new NodeHelper(node).getAttribute("name");
		return "{" + targetNamespace + "}" + name;
	}

    public static <T extends Referable> List<String> toIdentifiers(List<T> nodes){
        List<String> identifiers = new ArrayList<>();

        for(T node : nodes){
            identifiers.add(new ElementIdentifier(node.toXOM()).toIdentifier());
        }

        return identifiers;
    }

}
