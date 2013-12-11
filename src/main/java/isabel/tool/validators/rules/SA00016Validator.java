package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00016Validator extends Validator {

	public SA00016Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes partnerLinks = fileHandler.getBpel().getDocument()
				.query("//bpel:partnerLink", Standards.CONTEXT);
		for (Node partnerLink : partnerLinks) {
			NodeHelper partnerLinkHelper = new NodeHelper(partnerLink);
			if (partnerLinkHelper.hasNoAttribute("myRole")
					&& partnerLinkHelper.hasNoAttribute("partnerRole")) {
				addViolation(partnerLink);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 16;
	}
}
