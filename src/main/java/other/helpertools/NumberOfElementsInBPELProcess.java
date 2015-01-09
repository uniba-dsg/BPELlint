package other.helpertools;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Code partly taken from https://github.com/uniba-dsg/prope/blob/master/src/main/java/prope/metrics/installability/deployability/DeploymentPackageAnalyzer.java
 */
public class NumberOfElementsInBPELProcess {

    static DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

    public static void main(String[] args) throws IOException {
        List<String> lines = Files.walk(Paths.get("Testcases"), Integer.MAX_VALUE).
                filter((p) -> p.toString().endsWith(".bpel")).
                map((p) -> String.format("%s,%d", p.toString(), countElementsAccordingToPropeMethod(p))).
                collect(Collectors.toList());

        lines.stream().forEachOrdered(System.out::println);
    }

    private static int countElementsAccordingToPropeMethod(Path path) {
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(path.toFile());

            return countElementsAndAttributes(doc.getChildNodes());
        } catch (Exception e) {
            System.err.println("error in " + path + " reason: " + e.getMessage());
            return -1;
        }
    }

    private static int countElementsAndAttributes(NodeList nodes) {
        int sum = 0;
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            // count attributes and elements
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                // COUNT: An element, cost:1
                sum++;
                if (node.getChildNodes().getLength() > 0) {
                    sum += countElementsAndAttributes(node.getChildNodes());
                }

                NamedNodeMap attributes = node.getAttributes();
                for (int j = 0; j < attributes.getLength(); j++) {
                    Node attribute = attributes.item(j);
                    boolean isEmpty = attribute.getNodeValue().equals("");
                    boolean isNamespace = isNamespaceDeclaration(attribute);
                    if (!isEmpty && !isNamespace) {
                        // COUNT: An attribute that contains something, cost:1
                        sum++;
                    }
                }
            }

            // COUNT: Non-empty text node
            if (isNonEmptyTextNode(node)) {
                sum++;
            }
        }
        return sum;
    }

    private static boolean isNamespaceDeclaration(Node attribute) {
        return attribute.toString().startsWith("xmlns");
    }

    private static boolean isNonEmptyTextNode(Node node) {
        boolean isTextNode = node.getNodeType() == Node.TEXT_NODE;
        String nodeValue = node.getNodeValue();
        boolean isNonEmpty = !(nodeValue == null)
                && !("".equals(nodeValue.trim()));
        return isTextNode && isNonEmpty;
    }

}
