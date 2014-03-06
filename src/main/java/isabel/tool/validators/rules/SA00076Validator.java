package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.tool.validators.result.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00076Validator extends Validator {

	public SA00076Validator(ProcessContainer files,
	                        ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes forEachLoops = processContainer.getBpel().getDocument()
				.query("//bpel:forEach", Standards.CONTEXT);
		for (Node forEach : forEachLoops) {
			NodeHelper forEachHelper = new NodeHelper(forEach);

			Nodes variables = forEach.query(
					"bpel:scope/bpel:variables/bpel:variable",
					Standards.CONTEXT);
			for (Node variable : variables) {
				NodeHelper variableHelper = new NodeHelper(variable);

				if (forEachHelper.getAttribute("counterName").equals(
						variableHelper.getAttribute("name"))) {
					addViolation(variable);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 76;
	}
}
