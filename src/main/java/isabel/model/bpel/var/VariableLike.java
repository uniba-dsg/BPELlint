package isabel.model.bpel.var;

import isabel.model.NavigationException;
import isabel.model.Referable;
import isabel.model.wsdl.PropertyAliasElement;
import isabel.model.wsdl.PropertyElement;

public interface VariableLike extends Referable {

	String getVariableName();

	boolean hasVariableMessageType();

	String getVariableMessageType();

	boolean hasVariableElement();

	String getVariableElement();

	PropertyAliasElement resolvePropertyAlias(PropertyElement property) throws NavigationException;
}
