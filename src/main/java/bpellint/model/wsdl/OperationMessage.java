package bpellint.model.wsdl;

import bpellint.model.NavigationException;
import bpellint.model.Referable;

public interface OperationMessage extends Referable {

	public MessageElement getMessage() throws NavigationException;
	
}
