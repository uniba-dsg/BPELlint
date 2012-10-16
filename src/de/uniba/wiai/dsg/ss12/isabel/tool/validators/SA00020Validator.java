package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;

public class SA00020Validator extends Validator {

	public SA00020Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (DocumentEntry documentEntry : fileHandler.getAllWsdls()) {
			String filePath = documentEntry.getFilePath();
			for (Node propertyAlias : getPropertyAliases(documentEntry)) {
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
					addViolation(filePath, propertyAlias, 1);
				}
			}
		}
	}

	private Nodes getPropertyAliases(DocumentEntry documentEntry) {
		return documentEntry.getDocument().query("//vprop:propertyAlias",
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
