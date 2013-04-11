package isabel.tool.imports;

import isabel.tool.helper.NodeHelper;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.ParsingException;
import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Paths;
import java.util.Objects;

public class XmlFileLoader {

	private final Builder builder = new Builder(new LocationAwareNodeFactory());

	public XmlFile load(String file) throws ParsingException, IOException {
		Objects.requireNonNull(file, "file must not be null");

		Logger.info("Loading XML document from {0}", file);
		return new XmlFile(builder.build(new File(file)));
	}

	public XmlFile loadImportNode(Node importNode) throws ParsingException, IOException {
		// remove relative path elements like .. and .
		String nodeDirectory = getNodeDirectory(importNode);
		String importPath = getImportPath(importNode);
		String canonicalPath = Paths.get(nodeDirectory, importPath).toFile().getCanonicalPath();

		return load(canonicalPath);
	}

	private String getImportPath(Node node) {
		NodeHelper nodeHelper = new NodeHelper(node);

		if (nodeHelper.hasAttribute("schemaLocation")) {
			return Paths.get(nodeHelper.getAttribute("schemaLocation")).toString();
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


	public XmlFile loadFromResourceStream(String xmlschemaXsd) throws IOException, ParsingException {
		try (InputStream stream = ProcessContainerLoader.class.getResourceAsStream(xmlschemaXsd)) {
			Logger.info("Loading XML document via stream from {0}", xmlschemaXsd);

			Document document = builder.build(stream);
			Logger.debug("Loaded XML document via stream from {0}", document.getBaseURI());

			return new XmlFile(document);
		}
	}
}
