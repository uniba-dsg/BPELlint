package isabel.tool.validators.rules;

import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Node;
import nu.xom.Nodes;

import java.util.HashSet;
import java.util.Set;

import static isabel.model.Standards.CONTEXT;

public class SA00002Validator extends Validator {

	public SA00002Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (XmlFile wsdlEntry : fileHandler.getWsdls()) {
			for (Node portType : getPortTypes(wsdlEntry)) {
				Set<String> nameSet = new HashSet<>();
				for (Node nameAttribute : getOperationNames(portType)) {
					String currentName = nameAttribute.toXML();

					if (nameSet.contains(currentName)) {
						addViolation(nameAttribute);
					} else {
						nameSet.add(currentName);
					}
				}
			}
		}
	}

	private Nodes getOperationNames(Node portType) {
		return portType.query("child::wsdl:operation/@name", CONTEXT);
	}

	private Nodes getPortTypes(XmlFile wsdlEntry) {
		return wsdlEntry.getDocument().query("//wsdl:portType", CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 2;
	}
}
