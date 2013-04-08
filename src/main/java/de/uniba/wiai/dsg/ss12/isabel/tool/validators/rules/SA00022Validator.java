package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;

import java.util.List;

public class SA00022Validator extends Validator {

    private static final int SAME_MESSAGE_TYPE = 3;
    private static final int SAME_ELEMENT = 2;
    private static final int SAME_TYPE = 1;

    public SA00022Validator(BpelProcessFiles files,
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
                NodeHelper otherPropertyAliasHelper = new NodeHelper(otherPropertyAlias);

                if (!propertyAliasHelper.hasSameAttribute(otherPropertyAliasHelper, "propertyName")) {
                    continue;// go to next element
                }

	            if(propertyAliasHelper.hasSameAttribute(otherPropertyAliasHelper, "type")) {
					addViolation(propertyAliasHelper.getNode(),SAME_TYPE);
	            }
	            if(propertyAliasHelper.hasSameAttribute(otherPropertyAliasHelper, "element")) {
		            addViolation(propertyAliasHelper.getNode(),SAME_ELEMENT);
	            }
	            if(propertyAliasHelper.hasSameAttribute(otherPropertyAliasHelper, "messageType")) {
		            addViolation(propertyAliasHelper.getNode(),SAME_MESSAGE_TYPE);
	            }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 22;
    }
}