package bpellint.core.model;

import nu.xom.Attribute;
import nu.xom.Element;
import nu.xom.Node;
import nu.xom.ParentNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Node2Xpath {

    public static String getXpath(Node node) {
        return getXpath(node, "");
    }

    public static String getXpath(Node node, String xpath) {
        if (node == null) {
            return "";
        }

        ParentNode parent = node.getParent();

        if(node instanceof Attribute) {
            // this is a leaf, the xpath parameter can be ignored
            return getXpath(parent, String.format("/@%s", ((Attribute) node).getLocalName()));
        } else if(node instanceof Element) {
            Element element = (Element) node;
            String localName = element.getLocalName();
            if(parent == null) {
                return String.format("/%s%s", localName, xpath);
            } else {
                int childCount = parent.getChildCount();
                List<Element> nodes = new LinkedList<>();
                for(int i = 0; i < childCount; i++){
                    Node child = parent.getChild(i);
                    if(child instanceof Element) {
                        nodes.add((Element) child);
                    }
                }

                Map<String, List<Element>> result = nodes.stream().collect(Collectors.groupingBy(Element::getLocalName));


                List<Element> childrenOfSameTypeAsCurrentElement = result.get(localName);
                if (childrenOfSameTypeAsCurrentElement == null || childrenOfSameTypeAsCurrentElement.size() == 1) {
                    return getXpath(parent, String.format("/%s%s", localName, xpath));
                } else {
                    int index = childrenOfSameTypeAsCurrentElement.indexOf(element);
                    return getXpath(parent, String.format("/%s[%d]%s", localName, index, xpath));
                }
            }
        } else {
            // not covered
            return xpath;
        }
    }

}
