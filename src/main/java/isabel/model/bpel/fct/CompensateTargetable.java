package isabel.model.bpel.fct;

import isabel.model.Referable;

public interface CompensateTargetable extends Referable {

	boolean hasCompensationHandler();

	boolean hasFaultHandler();

	Referable getEnclosingFctBarrier();

}
