package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import static de.uniba.wiai.dsg.ss12.isabel.tool.validators.ValidatorNavigator.getAttributeValue;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00005Validator extends Validator {

	private String filePath;

	public SA00005Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		filePath = fileHandler.getBpel().getFilePath();
		Document bpelDom = fileHandler.getBpel().getDocument();

		Nodes receives = bpelDom.query("//bpel:receive", CONTEXT);
		checkPortTypeCorrectness(receives);

		Nodes replies = bpelDom.query("//bpel:reply", CONTEXT);
		checkPortTypeCorrectness(replies);

		Nodes invokes = bpelDom.query("//bpel:invoke", CONTEXT);
		checkPortTypeCorrectness(invokes);

		Nodes onEvents = bpelDom.query("//bpel:onEvent", CONTEXT);
		checkPortTypeCorrectness(onEvents);

		Nodes onMessages = bpelDom.query("//bpel:onMessage", CONTEXT);
		checkPortTypeCorrectness(onMessages);
	}

	private void checkPortTypeCorrectness(Nodes messageActivities) {
		for (Node messageActivity : messageActivities) {
			Node partnerLink;
			try {
				String partnerLinkAttribute = getAttributeValue(messageActivity
						.query("@partnerLink", CONTEXT));

				partnerLink = navigator.getPartnerLink(fileHandler.getBpel()
						.getDocument(), partnerLinkAttribute);

				Node correspondingPortType = navigator
						.partnerLinkToPortType(partnerLink);

				String localPortTypeDefinition = getLocalPortTypeDefinition(messageActivity);
				String correspondingPortTypeName = getAttributeValue(correspondingPortType
						.query("@name", CONTEXT));

				if (!correspondingPortTypeName.equals(localPortTypeDefinition)) {
					addViolation(filePath, messageActivity, 1);
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}
		}
	}

	private String getLocalPortTypeDefinition(Node messageActivity) {
		String localPortTypeDefinition = getAttributeValue(messageActivity
				.query("@portType", CONTEXT));
		if (localPortTypeDefinition.contains(":")) {
			localPortTypeDefinition = localPortTypeDefinition
					.substring(localPortTypeDefinition.indexOf(":") + 1);
		}
		return localPortTypeDefinition;
	}

	@Override
	public int getSaNumber() {
		return 5;
	}
}
