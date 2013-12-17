package isabel.model.bpel;

import isabel.model.Referable;

public interface VariableLike extends Referable {

	String getVariableName();
	
	boolean hasVariableMessageType();
	
	String getVariableMessageType();
	
	boolean hasVariableElement();

	String getVariableElement();
	
}
