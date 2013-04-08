package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00083Validator extends Validator {

    public SA00083Validator(BpelProcessFiles files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        Nodes faultHandlers = fileHandler.getBpel().getDocument().query("//bpel:eventHandlers", Standards.CONTEXT);
        for(Node faultHandler : faultHandlers){
            NodeHelper faultHandlerHelper = new NodeHelper(faultHandler);

            if(faultHandlerHelper.hasEmptyQueryResult("bpel:onEvent") && faultHandlerHelper.hasEmptyQueryResult("bpel:onAlarm")){
                addViolation(faultHandler);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 83;
    }
}
