package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Element;

public class SA00015Validator extends Validator {

    public SA00015Validator(BpelProcessFiles files, ValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        Element rootElement = this.fileHandler.getBpel().getDocument().getRootElement();
        NodeHelper processElement = new NodeHelper(rootElement);
        boolean hasReceiveWithCreateInstanceYes = processElement.hasQueryResult("//bpel:receive[@createInstance='yes']");
        boolean hasPickWithCreateInstanceYes = processElement.hasQueryResult("//bpel:pick[@createInstance='yes']");
        if(!hasReceiveWithCreateInstanceYes && !hasPickWithCreateInstanceYes){
            this.addViolation(rootElement);
        }
    }


    @Override
    public int getSaNumber() {
        return 15;
    }
}
