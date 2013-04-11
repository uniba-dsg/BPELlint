package isabel.tool.validators.rules;

import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;
import isabel.tool.imports.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00019Validator extends Validator {

	public static final int NEITHER_TYPE_NOR_ELEMENT = 1;
	public static final int TYPE_AND_ELEMENT = 2;

	public SA00019Validator(ProcessContainer files,
	                        ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		for (XmlFile wsdl : fileHandler.getWsdls()) {
			Nodes properties = wsdl.getDocument().query("//vprop:property",
					Standards.CONTEXT);
			for (Node property : properties) {
				NodeHelper propertyHelper = new NodeHelper(property);
				if (propertyHelper.hasNoAttribute("type")
						&& propertyHelper.hasNoAttribute("element")) {
					addViolation(property, NEITHER_TYPE_NOR_ELEMENT);
				} else if (propertyHelper.hasAttribute("type")
						&& propertyHelper.hasAttribute("element")) {
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
