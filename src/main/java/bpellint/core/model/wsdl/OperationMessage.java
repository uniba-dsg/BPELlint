package bpellint.core.model.wsdl;

import bpellint.core.model.NavigationException;
import bpellint.core.model.Referable;

public interface OperationMessage extends Referable {

	public MessageElement getMessage() throws NavigationException;
	
}
