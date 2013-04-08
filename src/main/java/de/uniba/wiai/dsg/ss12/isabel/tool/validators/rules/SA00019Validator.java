package de.uniba.wiai.dsg.ss12.isabel.tool.validators.rules;

import de.uniba.wiai.dsg.ss12.isabel.tool.impl.ValidationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.helper.NodeHelper;
import de.uniba.wiai.dsg.ss12.isabel.tool.impl.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.DocumentEntry;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00019Validator extends Validator {

    public static final int NEITHER_TYPE_NOR_ELEMENT = 1;
    public static final int TYPE_AND_ELEMENT = 2;

    public SA00019Validator(BpelProcessFiles files, ValidationCollector validationCollector) {
        super(files, validationCollector);
    }

    @Override
    public void validate() {
        for(DocumentEntry wsdl : fileHandler.getAllWsdls()){
            Nodes properties = wsdl.getDocument().query("//vprop:property", Standards.CONTEXT);
            for(Node property : properties){
                NodeHelper propertyHelper = new NodeHelper(property);
                if(propertyHelper.hasNoAttribute("type") && propertyHelper.hasNoAttribute("element")){
                    addViolation(property, NEITHER_TYPE_NOR_ELEMENT);
                } else if(propertyHelper.hasAttribute("type") && propertyHelper.hasAttribute("element")){
                    addViolation(property, TYPE_AND_ELEMENT);
                }
            }
        }
    }

    @Override
    public int getSaNumber() {
        return 19;
    }
}
