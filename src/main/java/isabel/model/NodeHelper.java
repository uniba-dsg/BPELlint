package isabel.model;

import isabel.model.bpel.VariableElement;
import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.Objects;

import static isabel.model.Standards.CONTEXT;

public class NodeHelper implements Referable{

    protected Node node;

    public NodeHelper(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("given node should not be null");
        }

        this.node = node;
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

    public boolean hasSameAttribute(NodeHelper otherNode, String attributeName) {
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

    public boolean hasNoAttribute(String name) {
        return !hasAttribute(name);
    }

    public int getAmountOfAttributes() {
        Nodes attributes = node.query("@*");
        return attributes.size();
    }

    public boolean hasAttributes() {
        return getAmountOfAttributes() > 0;
    }

    public NodeHelper getFirstChildElement() throws NavigationException {
        int amountOfChildren = getAmountOfChildern();
        if (amountOfChildren == 0) {
            throw new NavigationException("Node has no child.");
        }
        Node child = node.query("./*").get(0);
        if (!(child instanceof Element)) {
            throw new NavigationException("Node is not an element.");
        }

        return new NodeHelper(child);
    }

    public int getAmountOfChildern() {
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

    public Node getNode() {
        return node;
    }

    public static Element toElement(Node node) {
        return new NodeHelper(node).asElement();
    }

    public String getIdentifier() {
        return new ElementIdentifier(asElement()).toIdentifier();
    }

    public VariableElement getVariableByName(String variableName) throws NavigationException {
        Objects.requireNonNull(variableName, "VariableName must not be null!");

        NodeHelper element = new NodeHelper(node);
        String elementName = element.getLocalName();

        if ("scope".equals(elementName)
                || "process".equals(elementName)) {
            Nodes variable = node.query(
                    "./bpel:variables/bpel:variable[@name='" + variableName
                            + "']", CONTEXT);
            if (variable != null && !variable.isEmpty()) {
                return new VariableElement(variable.get(0));
            }
            if ("process".equals(elementName)) {
                throw new NavigationException("Variable does not exist.");
            }
        }

        if ("onEvent".equals(elementName)) {
            if (variableName.equals(element.getAttribute("variable"))) {
                return new VariableElement(node);
            }
        }
        if ("catch".equals(elementName)) {
            if (variableName.equals(element.getAttribute("faultVariable"))) {
                return new VariableElement(node);
            }
        }

        return getParent().getVariableByName(variableName);
    }

    public NodeHelper getParent() {
        return new NodeHelper(node.getParent());
    }

    @Override
    public Node toXOM() {
        return asElement();
    }
}
