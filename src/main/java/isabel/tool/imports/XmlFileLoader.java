package isabel.tool.imports;

import isabel.model.XmlFile;
import isabel.model.NodeHelper;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.ParsingException;
import org.pmw.tinylog.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class XmlFileLoader {

	private final Builder builder = new Builder(new LocationAwareNodeFactory());

	public XmlFile load(Path path) throws ParsingException, IOException {
		Objects.requireNonNull(path, "file must not be null");

		Logger.info("Loading XML document from {0}", path);
		return new XmlFile(builder.build(path.toFile()));
	}

	public XmlFile loadImportNode(Node importNode) throws ParsingException, IOException {
		// remove relative path elements like .. and .
		Path nodeDirectory = getNodeDirectory(importNode);
		String importPath = getImportPath(importNode);

		return load(nodeDirectory.resolve(importPath).normalize());
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

	private Path getNodeDirectory(Node node) {
		if (node.getBaseURI().isEmpty()) {
			return null;
		}

		return Paths.get(URI.create(node.getBaseURI())).getParent();
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
