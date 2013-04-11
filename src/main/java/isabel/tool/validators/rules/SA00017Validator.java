package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import nu.xom.Document;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00017Validator extends Validator {

	public SA00017Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Document document = fileHandler.getBpel().getDocument();
		Nodes partnerLinks = document.query("//bpel:partnerLink",
				Standards.CONTEXT);
		for (Node partnerLink : partnerLinks) {
			NodeHelper partnerLinkHelper = new NodeHelper(partnerLink);
			if (partnerLinkHelper.hasAttribute("initializePartnerRole")
					&& partnerLinkHelper.hasNoAttribute("partnerRole")) {
				addViolation(partnerLink);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 17;
	}
}
