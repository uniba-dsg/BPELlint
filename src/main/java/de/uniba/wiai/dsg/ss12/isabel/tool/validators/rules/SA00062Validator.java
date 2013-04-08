package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00062Validator extends Validator {

    public SA00062Validator(BpelProcessFiles files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        Nodes picksWithCreateInstanceYes = fileHandler.getBpel().getDocument().query("//bpel:pick[@createInstance='yes']", Standards.CONTEXT);
        for(Node pick : picksWithCreateInstanceYes){
            Nodes onAlarms = pick.query("bpel:onAlarm", Standards.CONTEXT);
            for(Node onAlarm : onAlarms){
                addViolation(onAlarm);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 62;
    }
}
