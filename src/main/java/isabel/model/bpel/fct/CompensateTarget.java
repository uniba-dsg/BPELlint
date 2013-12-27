package isabel.model.bpel.fct;

import isabel.model.Referable;

public interface CompensateTarget extends Referable {

	boolean hasCompensationHandler();

	boolean hasFaultHandler();

}
