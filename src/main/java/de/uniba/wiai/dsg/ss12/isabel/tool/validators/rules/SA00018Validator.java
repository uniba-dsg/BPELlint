package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;

public class SA00018Validator extends Validator {

	public SA00018Validator(BpelProcessFiles files,
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
				String name = new NodeHelper(partnerLink).getAttribute("name");
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
