package bpellint.core.model.bpel.fct;

import static bpellint.core.model.Standards.CONTEXT;
import bpellint.core.model.NodeHelper;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.Referable;
import bpellint.core.model.bpel.ProcessElement;
import bpellint.core.model.bpel.ScopeElement;

public interface CompensateTargetable extends Referable {

	default boolean hasCompensationHandler() {
		return toXOM().query("./bpel:compensationHandler", CONTEXT).hasAny();
	}

	default boolean hasFaultHandler() {
		return toXOM().query("./bpel:faultHandlers", CONTEXT).hasAny();
	}

	default Referable getEnclosingFctBarrier() {
		NodeHelper parent = new NodeHelper(toXOM());
		while (!"process".equals(parent.getLocalName())) {
			parent = parent.getParent();
			String localName = parent.getLocalName();
			if ("scope".equals(localName)) {
				return new ScopeElement(parent, getProcessContainer());
			}
			if ("catch".equals(parent.getLocalName())) {
				return new CatchElement(parent.toXOM(), getProcessContainer());
			}
			if ("catchAll".equals(parent.getLocalName())) {
				return new CatchAllElement(parent.toXOM(), getProcessContainer());
			}
			if ("compensationHandler".equals(parent.getLocalName())) {
				return new CompensationHandlerElement(parent.toXOM(), getProcessContainer());
			}
			if ("terminationHandler".equals(parent.getLocalName())) {
				return new TerminationHandlerElement(parent.toXOM(), getProcessContainer());
			}
		}
		return new ProcessElement(parent, getProcessContainer());
	}

	ProcessContainer getProcessContainer();
}
