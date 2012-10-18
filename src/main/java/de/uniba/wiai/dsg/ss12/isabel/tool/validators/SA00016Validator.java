package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00016Validator extends Validator {

    public SA00016Validator(BpelProcessFiles files, ValidationResult violationCollector) {
        super(files, violationCollector);
    }

    @Override
    public void validate() {
        Nodes partnerLinks = fileHandler.getBpel().getDocument().query("//bpel:partnerLink", Standards.CONTEXT);
        for(Node partnerLink : partnerLinks){
            NodeHelper partnerLinkHelper = new NodeHelper(partnerLink);
            if(partnerLinkHelper.hasNoAttribute("myRole") && partnerLinkHelper.hasNoAttribute("partnerRole")){
                addViolation(partnerLink);
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 16;
    }
}
