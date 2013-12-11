package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.bpel.CompensateElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import nu.xom.Node;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00008Validator extends Validator {

	public SA00008Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (CompensateElement compensate : fileHandler.getAllCompensates()) {

            if (!compensate.isWithinFaultHandler()
                    && !compensate.isWithinCompensationHandler()
                    && !compensate.isWithinTerminationHandler()) {
                addViolation(compensate);
            }
		}
	}

	@Override
	public int getSaNumber() {
		return 8;
	}

}
