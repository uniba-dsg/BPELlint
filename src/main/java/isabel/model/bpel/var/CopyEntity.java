package isabel.model.bpel.var;

import isabel.model.NavigationException;
import isabel.model.bpel.PartnerLinked;
import isabel.model.wsdl.PropertyAliasElement;

public interface CopyEntity extends PartnerLinked {

	boolean isEmpty();

	boolean isMessageVariableAssignment();

	boolean isPartnerLinkAssignment();

	boolean isVariableAssignment();

	boolean isQueryResultAssignment();

	boolean isLiteralAssignment();

	PropertyAliasElement getVariablePropertyAlias() throws NavigationException;
}
