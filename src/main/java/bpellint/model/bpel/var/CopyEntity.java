package bpellint.model.bpel.var;

import bpellint.model.NavigationException;
import bpellint.model.bpel.PartnerLinked;
import bpellint.model.wsdl.PropertyAliasElement;

public interface CopyEntity extends PartnerLinked {

	boolean isEmpty();

	boolean isMessageVariableAssignment();

	boolean isPartnerLinkAssignment();

	boolean isVariableAssignment();

	boolean isQueryResultAssignment();

	boolean isLiteralAssignment();

	PropertyAliasElement getVariablePropertyAlias() throws NavigationException;
}
