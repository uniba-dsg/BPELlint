package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00008Validator extends Validator {

	public SA00008Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
        for (Node compensateScope : getAllCompensates()) {
            NodeHelper compensateScopeHelper = new NodeHelper(compensateScope);

            if (!compensateScopeHelper.hasAncestor("faultHandlers") &&
                    !compensateScopeHelper.hasAncestor("compensationHandler") &&
                    !compensateScopeHelper.hasAncestor("terminationHandlers")) {
                addViolation(compensateScope);
            }
        }
	}

    private Nodes getAllCompensates() {
        return fileHandler.getBpel().getDocument()
                .query("//bpel:compensate", CONTEXT);
    }

	@Override
	public int getSaNumber() {
		return 8;
	}

}
