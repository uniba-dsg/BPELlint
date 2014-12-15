package bpellint.core.model.bpel.var;

import bpellint.core.model.NavigationException;
import bpellint.core.model.bpel.PartnerLinked;
import bpellint.core.model.wsdl.PropertyAliasElement;

public interface CopyEntity extends PartnerLinked {

	boolean isEmpty();

	boolean isMessageVariableAssignment();

	boolean isPartnerLinkAssignment();

	boolean isVariableAssignment();

	boolean isQueryResultAssignment();

	boolean isLiteralAssignment();

	PropertyAliasElement getVariablePropertyAlias() throws NavigationException;
}
