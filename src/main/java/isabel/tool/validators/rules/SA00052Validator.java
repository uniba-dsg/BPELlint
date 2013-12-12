package isabel.tool.validators.rules;

import isabel.model.ProcessContainer;
import isabel.model.bpel.mex.InvokeElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Nodes;

import static isabel.model.Standards.CONTEXT;

public class SA00052Validator extends Validator {
    public SA00052Validator(ProcessContainer files,
                            ValidationCollector violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {


        for (InvokeElement invoke : fileHandler.getAllInvokes()) {
            Nodes fromParts = invoke.toXOM().query("bpel:fromParts", CONTEXT);
            String outputVariable = invoke.getOutputVariableAttribute();

            if (!outputVariable.isEmpty() && fromParts.hasAny()) {
                addViolation(invoke);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 52;
    }
}
