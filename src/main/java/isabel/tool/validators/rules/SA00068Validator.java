package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

import java.util.HashSet;
import java.util.Set;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00068Validator extends Validator {

	public SA00068Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes sourcesNodes = this.fileHandler.getBpel().getDocument()
				.query("//bpel:sources", Standards.CONTEXT);
		for (Node sources : sourcesNodes) {
			checkLinkNameUniqueness(sources);
		}
	}

	private void checkLinkNameUniqueness(Node sources) {
		Set<String> uniqueNames = new HashSet<>();
		for (Node source : sources.query("./bpel:source",
				Standards.CONTEXT)) {
			String name = new NodeHelper(source).getAttribute("linkName");
			
			if (uniqueNames.contains(name)) {
				addViolation(source);
			} else {
				uniqueNames.add(name);
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 68;
	}

}
