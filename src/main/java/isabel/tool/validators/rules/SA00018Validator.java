package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.model.bpel.PartnerLinkElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.HashSet;
import java.util.Set;

public class SA00018Validator extends Validator {

	public SA00018Validator(ProcessContainer files,
	                        ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes allPartnerLinks = this.fileHandler.getBpel().getDocument()
				.query("//bpel:partnerLinks", Standards.CONTEXT);
		for (Node partnerLinks : allPartnerLinks) {
			Set<String> uniqueNames = new HashSet<>();
			for (Node partnerLink : partnerLinks.query("bpel:partnerLink",
					Standards.CONTEXT)) {
				String name = new PartnerLinkElement(partnerLink).getName();
				if (uniqueNames.contains(name)) {
					addViolation(partnerLink);
				} else {
					uniqueNames.add(name);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 18;
	}
}
