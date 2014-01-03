package isabel.model.bpel.var;

import isabel.model.Referable;

public interface CopyEntity extends Referable {
	
	boolean isEmpty();

	boolean isMessageVariableAssignment();

	boolean isPartnerLinkAssignment();

	boolean isVariableAssignment();

	boolean isQueryResultAssignment();

	boolean isLiteralAssignment();
	
}
