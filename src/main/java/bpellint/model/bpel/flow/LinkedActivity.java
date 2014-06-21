package bpellint.model.bpel.flow;


import java.util.List;

import bpellint.model.Referable;
import bpellint.model.bpel.OptionalElementNotPresentException;

public interface LinkedActivity extends Referable {

	List<SourceElement> getSourceElements() throws OptionalElementNotPresentException;

	List<TargetElement> getTargetElements() throws OptionalElementNotPresentException;

}
