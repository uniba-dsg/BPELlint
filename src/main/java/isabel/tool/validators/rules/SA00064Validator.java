package isabel.tool.validators.rules;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

public class SA00064Validator extends Validator {

	public SA00064Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes linksNodes = this.fileHandler.getBpel().getDocument()
				.query("//bpel:links", Standards.CONTEXT);
		for (Node links : linksNodes) {
			checkLinkeNameUniqueness(links);
		}
	}

	private void checkLinkeNameUniqueness(Node links) {
		Set<String> uniqueNames = new HashSet<>();
		for (Node link : links.query("./bpel:link",
				Standards.CONTEXT)) {
			String name = new NodeHelper(link).getAttribute("name");
			
			if (uniqueNames.contains(name)) {
				addViolation(link);
			} else {
				uniqueNames.add(name);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 64;
	}

}
