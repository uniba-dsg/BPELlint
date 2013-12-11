package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00020Validator extends Validator {

	public SA00020Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (XmlFile xmlFile : fileHandler.getWsdls()) {
			for (Node propertyAlias : getPropertyAliases(xmlFile)) {
				boolean messageTypeAndPart = messageTypeExists(propertyAlias)
						&& partExists(propertyAlias)
						&& !typeExists(propertyAlias)
						&& !elementExists(propertyAlias);
				boolean type = !messageTypeExists(propertyAlias)
						&& !partExists(propertyAlias)
						&& typeExists(propertyAlias)
						&& !elementExists(propertyAlias);
				boolean element = !messageTypeExists(propertyAlias)
						&& !partExists(propertyAlias)
						&& !typeExists(propertyAlias)
						&& elementExists(propertyAlias);
				if (!(messageTypeAndPart || type || element)) {
					addViolation(propertyAlias);
				}
			}
		}
	}

	private Nodes getPropertyAliases(XmlFile xmlFile) {
		return xmlFile.getDocument().query("//vprop:propertyAlias",
				CONTEXT);
	}

	private boolean messageTypeExists(Node propertyAlias) {
		return new NodeHelper(propertyAlias).hasAttribute("messageType");
	}

	private boolean partExists(Node propertyAlias) {
		return new NodeHelper(propertyAlias).hasAttribute("part");
	}

	private boolean typeExists(Node propertyAlias) {
		return new NodeHelper(propertyAlias).hasAttribute("type");
	}

	private boolean elementExists(Node propertyAlias) {
		return new NodeHelper(propertyAlias).hasAttribute("element");
	}

	@Override
	public int getSaNumber() {
		return 20;
	}
}
