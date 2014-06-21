package bpellint.model.bpel.fct;


import java.util.List;

import bpellint.model.Referable;
import bpellint.model.bpel.ScopeElement;

public interface FctHandler extends Referable {

	List<ScopeElement> getRootScopes();

}
