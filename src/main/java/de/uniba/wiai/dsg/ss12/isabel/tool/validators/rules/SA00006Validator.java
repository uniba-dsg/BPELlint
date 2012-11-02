package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

import static de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards.CONTEXT;

public class SA00006Validator extends Validator {

	public SA00006Validator(BpelProcessFiles files,
			ValidationResult violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
        for (Node rethrow : getAllRethrows()) {
            NodeHelper rethrowHelper = new NodeHelper(rethrow);

            if (!rethrowHelper.hasAncestor("bpel:faultHandlers")) {
				addViolation(rethrow);
			}
		}
	}

    private Nodes getAllRethrows() {
        return fileHandler.getBpel().getDocument()
                .query("//bpel:rethrow", CONTEXT);
    }

	@Override
	public int getSaNumber() {
		return 6;
	}

}