package isabel.model.bpel.var;

import isabel.model.bpel.PartnerLinked;

public interface CopyEntity extends PartnerLinked {
	
	boolean isEmpty();

	boolean isMessageVariableAssignment();

	boolean isPartnerLinkAssignment();

	boolean isVariableAssignment();

	boolean isQueryResultAssignment();

	boolean isLiteralAssignment();
	
}
