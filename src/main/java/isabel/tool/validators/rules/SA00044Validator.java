package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import java.util.HashSet;
import java.util.Set;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;

public class SA00044Validator extends Validator {

	public SA00044Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (Node correlationSetContainer : getCorrelationSetContainers()) {
			Set<String> names = new HashSet<>();
			for (Node correlationSet : correlationSetContainer.query(
					"bpel:correlationSet", CONTEXT)) {
				String name = new NodeHelper(correlationSet)
						.getAttribute("name");

				if (names.contains(name)) {
					addViolation(correlationSet);
				} else {
					names.add(name);
				}
			}
		}
	}

	private Nodes getCorrelationSetContainers() {
		return fileHandler.getBpel().getDocument()
				.query("//bpel:correlationSets", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 44;
	}
}
