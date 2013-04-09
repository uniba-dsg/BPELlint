package isabel.tool.validators.rules;

import java.util.List;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.ValidationCollector;

public class SA00022Validator extends Validator {

	private static final int SAME_MESSAGE_TYPE = 3;
	private static final int SAME_ELEMENT = 2;
	private static final int SAME_TYPE = 1;

	public SA00022Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		List<Node> allPropertyAliases = fileHandler.getAllPropertyAliases();
		for (int i = 0, allPropertyAliasesSize = allPropertyAliases.size(); i < allPropertyAliasesSize; i++) {

			Node propertyAlias = allPropertyAliases.get(i);
			NodeHelper propertyAliasHelper = new NodeHelper(propertyAlias);

			for (int j = i + 1; j < allPropertyAliasesSize; j++) {

				Node otherPropertyAlias = allPropertyAliases.get(j);
				NodeHelper otherPropertyAliasHelper = new NodeHelper(
						otherPropertyAlias);

				if (!propertyAliasHelper.hasSameAttribute(
						otherPropertyAliasHelper, "propertyName")) {
					continue;// go to next element
				}

				if (propertyAliasHelper.hasSameAttribute(
						otherPropertyAliasHelper, "type")) {
					addViolation(propertyAliasHelper.getNode(), SAME_TYPE);
				}
				if (propertyAliasHelper.hasSameAttribute(
						otherPropertyAliasHelper, "element")) {
					addViolation(propertyAliasHelper.getNode(), SAME_ELEMENT);
				}
				if (propertyAliasHelper.hasSameAttribute(
						otherPropertyAliasHelper, "messageType")) {
					addViolation(propertyAliasHelper.getNode(),
							SAME_MESSAGE_TYPE);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 22;
	}
}