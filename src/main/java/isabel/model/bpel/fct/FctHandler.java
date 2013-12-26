package isabel.model.bpel.fct;

import isabel.model.Referable;
import isabel.model.bpel.ScopeElement;

import java.util.List;

public interface FctHandler extends Referable {

	List<ScopeElement> getRootScopes();

}
