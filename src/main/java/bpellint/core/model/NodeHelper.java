package bpellint.core.model;

import nu.xom.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class NodeHelper implements Referable {

    protected final Node node;

    public NodeHelper(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("given node should not be null");
        }

        this.node = node;
    }

    public NodeHelper(Referable referable) {
		this(referable.toXOM());
	}

    /**
     * Create a {@link NodeHelper} and check that <code>node</code> name equals
     * <code>type</code>
     *
     * @param node the {@link NodeHelper} operates on this {@link Node}
     * @param type equals {@link NodeHelper}<code>.getLocalName()</code>
     * @throws IllegalArgumentException if the name <code>node</code> element is not equal to
     *                                  <code>type</code>.
     */
    public NodeHelper(Node node, String type) {
        Objects.requireNonNull(type, "null is no type");
        Objects.requireNonNull(node, "Expected <" + type + "> element.");
        this.node = node;
        if (!type.equals(getLocalName())) {
            throw new IllegalArgumentException("Expect a <" + type + "> element. But was " + getLocalName());
        }
    }

    public String getLocalName() {
        Element el = (Element) node;
        return el.getLocalName();
    }

    public boolean hasLocalName(String name) {
        return getLocalName().equals(name);
    }

    public String getTargetNamespace() {
        return new NodeHelper(node.getDocument().getRootElement())
                .getAttribute("targetNamespace");
    }

    public String getFilePath() {
        return node.getBaseURI();
    }

    public boolean hasTargetNamespace(String targetNamespace) {
        return getTargetNamespace().equals(targetNamespace);
    }

    public boolean hasTargetNamespace(Node otherNode) {
        return hasTargetNamespace(new NodeHelper(otherNode)
                .getTargetNamespace());
    }

    public String getAttribute(String name) {
        String result = this.asElement().getAttributeValue(name);
        if (result == null) {
            return "";
        } else {
            return result;
        }
    }

    public Attribute getAttributeNode(String name) {
        Nodes attributes = node.query("@" + name);

        if (attributes.hasAny()) {
            return (Attribute) attributes.get(0);
        }
        return null;
    }

    public boolean hasSameAttribute(Referable otherReferable, String attributeName) {
    	NodeHelper otherNode = new NodeHelper(otherReferable);
        if (hasAttribute(attributeName)
                && otherNode.hasAttribute(attributeName)) {

            AttributeHelper otherAttribute = new AttributeHelper(otherNode.getAttributeNode(attributeName));
            AttributeHelper attribute = new AttributeHelper(getAttributeNode(attributeName));
            if (attribute.isEqualTo(otherAttribute.getAttribute())) {
                return true;
            }

        }
        return false;
    }

    public boolean hasAttribute(String name) {
        return !getAttribute(name).isEmpty();
    }

    public int getAmountOfAttributes() {
        Nodes attributes = node.query("@*");
        return attributes.size();
    }

    public boolean hasAttributes() {
        return getAmountOfAttributes() > 0;
    }

    public NodeHelper getFirstChildElement() throws NavigationException {
        int amountOfChildren = getAmountOfChildren();
        if (amountOfChildren == 0) {
            throw new NavigationException("Node has no child.");
        }
        Node child = node.query("./*").get(0);
        if (!(child instanceof Element)) {
            throw new NavigationException("Node is not an element.");
        }

        return new NodeHelper(child);
    }

    public int getAmountOfChildren() {
        Nodes directChildren = node.query("./*");
        return directChildren.size();
    }

    public boolean hasNoAttributes() {
        return !hasAttributes();
    }

    public boolean hasQueryResult(String query) {
        return node.query(query, Standards.CONTEXT).hasAny();
    }

    public boolean hasEmptyQueryResult(String query) {
        return node.query(query, Standards.CONTEXT).isEmpty();
    }

    public boolean hasAncestor(String name) {
        return hasQueryResult("ancestor::" + name);
    }

    public Element asElement() {
        if (!(node instanceof Element)) {
            throw new IllegalArgumentException("Given Node must not be null or an attribute.");
        }

        return (Element) node;
    }

    public static Element toElement(Node node) {
        return new NodeHelper(node).asElement();
    }

    public NodeHelper getParent() {
        return new NodeHelper(node.getParent());
    }

    @Override
    public Node toXOM() {
        return asElement();
    }

    public boolean hasNoChildren() {
        return getAmountOfChildren() == 0;
    }

    public boolean hasContent() {
        List<String> valueList = getValueLines(asElement());

        Nodes children = asElement().query("./child::*");
        for (Node child : children) {
            valueList.removeAll(getValueLines((Element) child));
        }

        return !valueList.isEmpty();
    }

    private List<String> getValueLines(Element element) {
        String[] lines = element.getValue().split("\n");
        List<String> valueList = new LinkedList<>();
        for (String line : lines) {
            if (!line.trim().isEmpty()){
                valueList.add(line.trim());
            }
        }
        return valueList;
    }

    public String getXpath() {
        return getXpath(node, "");
    }

    public static String getXpath(Node node, String xpath) {
        if (node == null) {
            return "";
        }
        String elementName = "";
        if (node instanceof Element) {
            elementName = ((Element) node).getLocalName();
        }
        Node parent = node.getParent();
        if (parent == null) {
            return xpath;
        }
        int index = ((ParentNode) parent).indexOf(node);
        return getXpath(parent, String.format("/%s[%d]%s", elementName, index, xpath));
    }

}
