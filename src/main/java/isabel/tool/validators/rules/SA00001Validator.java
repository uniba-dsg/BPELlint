package isabel.tool.validators.rules;

import static isabel.tool.impl.Standards.CONTEXT;

import java.util.List;

import isabel.tool.imports.ProcessContainer;
import nu.xom.Node;
import isabel.tool.helper.NodesUtil;
import isabel.tool.helper.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.XmlFile;

public class SA00001Validator extends Validator {

	private static final int SOLICIT_RESPONSE_TYPE = 2;
	private static final int NOTIFICATION_FAULT = 1;

	public SA00001Validator(ProcessContainer files,
			ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (XmlFile wsdlEntry : fileHandler.getAllWsdls()) {
			for (Node currentOperation : getOperations(wsdlEntry)) {
				OperationElement operationHelper = new OperationElement(
						currentOperation);
				if (operationHelper.isNotification()) {
					addViolation(currentOperation, NOTIFICATION_FAULT);
				}
				if (operationHelper.isSolicitResponse()) {
					addViolation(currentOperation, SOLICIT_RESPONSE_TYPE);
				}
			}
		}
	}

	private List<Node> getOperations(XmlFile wsdlEntry) {
		return NodesUtil.toList(wsdlEntry.getDocument().query(
				"//wsdl:portType/wsdl:operation", CONTEXT));
	}

	@Override
	public int getSaNumber() {
		return 1;
	}

}
