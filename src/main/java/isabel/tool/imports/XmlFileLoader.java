package isabel.tool.imports;

import isabel.tool.helper.NodeHelper;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.ParsingException;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class XmlFileLoader {

	private final Builder builder = new Builder(new LocationAwareNodeFactory());

	public XmlFile loadImportNode(Node importNode)
			throws ParsingException, IOException {
		// remove relative path elements like .. and .
		String canonicalPath = Paths.get(getNodeDirectory(importNode),
				getImportPath(importNode)).toFile().getCanonicalPath();

		Document document = builder.build(canonicalPath);

		return new XmlFile(document);
	}

	private String getImportPath(Node node) {
		NodeHelper nodeHelper = new NodeHelper(node);

		if (nodeHelper.hasAttribute("schemaLocation")) {
			return Paths.get(nodeHelper.getAttribute("schemaLocation"))
					.toString();
		} else if (nodeHelper.hasAttribute("location")) {
			return Paths.get(nodeHelper.getAttribute("location")).toString();
		}

		return null;
	}

	private String getNodeDirectory(Node node) {
		if (node.getBaseURI().isEmpty()) {
			return null;
		}

		return Paths.get(URI.create(node.getBaseURI())).getParent().toString();
	}


}
