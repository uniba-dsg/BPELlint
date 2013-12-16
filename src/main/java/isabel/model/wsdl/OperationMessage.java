package isabel.model.wsdl;

import isabel.model.NavigationException;
import isabel.model.Referable;

public interface OperationMessage extends Referable {

	public MessageElement getMessage() throws NavigationException;
	
}
