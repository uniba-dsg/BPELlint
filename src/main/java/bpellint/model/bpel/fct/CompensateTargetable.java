package bpellint.model.bpel.fct;

import bpellint.model.Referable;

public interface CompensateTargetable extends Referable {

	boolean hasCompensationHandler();

	boolean hasFaultHandler();

	Referable getEnclosingFctBarrier();

}
