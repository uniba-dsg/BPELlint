package bpellint.core.model.bpel.fct;


import java.util.List;

import bpellint.core.model.Referable;
import bpellint.core.model.bpel.ScopeElement;

public interface FctHandler extends Referable {

	List<ScopeElement> getRootScopes();

}
