package isabel.model.bpel.flow;

import isabel.model.Referable;
import isabel.model.bpel.OptionalElementNotPresentException;

import java.util.List;

public interface LinkedActivity extends Referable {

	List<SourceElement> getSourceElements() throws OptionalElementNotPresentException;

	List<TargetElement> getTargetElements() throws OptionalElementNotPresentException;

}
