package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.List;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00022Validator extends Validator {

	private static final int SAME_MESSAGE_TYPE = 3;
	private static final int SAME_ELEMENT = 2;
	private static final int SAME_TYPE = 1;

	public SA00022Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {

		List<DocumentEntry> wsdls = fileHandler.getAllWsdls();

		for (DocumentEntry documentEntry : wsdls) {

			String fileName = documentEntry.getFilePath();

			Nodes propertyAliases = documentEntry.getDocument().query(
					"//*[name()='vprop:propertyAlias']", CONTEXT);

			if (propertyAliases.size() > 1) {
				for (int i = 0; i < propertyAliases.size(); i++) {
					Node currentPropertyAlias = propertyAliases.get(i);

					for (int j = i + 1; j < propertyAliases.size(); j++) {
						Node otherPropertyAlias = propertyAliases.get(j);

						Nodes currentPropertyName = currentPropertyAlias.query(
								"@propertyName", CONTEXT);
						Nodes otherPropertyName = otherPropertyAlias.query(
								"@propertyName", CONTEXT);

						if (currentPropertyName.size() > 0
								&& otherPropertyName.size() > 0) {

							if (currentPropertyName
									.get(0)
									.getValue()
									.equals(otherPropertyName.get(0).getValue())) {

								Nodes currentType = currentPropertyAlias.query(
										"@type", CONTEXT);
								Nodes otherType = otherPropertyAlias.query(
										"@type", CONTEXT);
								Nodes currentElement = currentPropertyAlias
										.query("@element", CONTEXT);
								Nodes otherElement = otherPropertyAlias.query(
										"@element", CONTEXT);
								Nodes currentMessageType = currentPropertyAlias
										.query("@messageType", CONTEXT);
								Nodes otherMessageType = otherPropertyAlias
										.query("@messageType", CONTEXT);

								if (currentType.size() > 0
										&& otherType.size() > 0) {
									if (currentType
											.get(0)
											.getValue()
											.equals(otherType.get(0).getValue())) {

										addViolation(fileName,
												currentPropertyAlias, SAME_TYPE);
									}
								}

								else if (currentElement.size() > 0
										&& otherElement.size() > 0) {
									if (currentElement
											.get(0)
											.getValue()
											.equals(otherElement.get(0)
													.getValue())) {
										addViolation(fileName,
												currentPropertyAlias, SAME_ELEMENT);
									}

								} else if (currentMessageType.size() > 0
										&& otherMessageType.size() > 0) {
									if (currentMessageType
											.get(0)
											.getValue()
											.equals(otherMessageType.get(0)
													.getValue())) {
										addViolation(fileName,
												currentPropertyAlias, SAME_MESSAGE_TYPE);
									}

								}
							}

						}

					}
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 22;
	}
}