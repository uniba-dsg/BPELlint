package bpellint.core.model.bpel.fct;

import bpellint.core.model.Referable;

public interface CompensateTargetable extends Referable {

	boolean hasCompensationHandler();

	boolean hasFaultHandler();

	Referable getEnclosingFctBarrier();

}
