package isabel.tool.validators.rules;

import isabel.model.NodesUtil;
import isabel.model.wsdl.OperationElement;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;
import isabel.model.XmlFile;
import nu.xom.Node;

import java.util.List;

import static isabel.model.Standards.CONTEXT;

public class SA00001Validator extends Validator {

	private static final int SOLICIT_RESPONSE_TYPE = 2;
	private static final int NOTIFICATION_FAULT = 1;

	public SA00001Validator(ProcessContainer files,
	                        ValidationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		for (XmlFile wsdlEntry : fileHandler.getWsdls()) {
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
