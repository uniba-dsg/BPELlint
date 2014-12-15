package bpellint.core.model.bpel.flow;


import java.util.List;

import bpellint.core.model.Referable;
import bpellint.core.model.bpel.OptionalElementNotPresentException;

public interface LinkedActivity extends Referable {

	List<SourceElement> getSourceElements() throws OptionalElementNotPresentException;

	List<TargetElement> getTargetElements() throws OptionalElementNotPresentException;

}
