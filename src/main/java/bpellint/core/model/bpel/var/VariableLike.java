package bpellint.core.model.bpel.var;

import bpellint.core.model.NavigationException;
import bpellint.core.model.Referable;
import bpellint.core.model.wsdl.PropertyAliasElement;
import bpellint.core.model.wsdl.PropertyElement;

public interface VariableLike extends Referable {

	String getVariableName();

	boolean hasVariableMessageType();

	String getVariableMessageType();

	boolean hasVariableElement();

	String getVariableElement();

	PropertyAliasElement resolvePropertyAlias(PropertyElement property) throws NavigationException;
}
