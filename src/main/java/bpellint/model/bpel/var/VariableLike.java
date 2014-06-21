package bpellint.model.bpel.var;

import bpellint.model.NavigationException;
import bpellint.model.Referable;
import bpellint.model.wsdl.PropertyAliasElement;
import bpellint.model.wsdl.PropertyElement;

public interface VariableLike extends Referable {

	String getVariableName();

	boolean hasVariableMessageType();

	String getVariableMessageType();

	boolean hasVariableElement();

	String getVariableElement();

	PropertyAliasElement resolvePropertyAlias(PropertyElement property) throws NavigationException;
}
