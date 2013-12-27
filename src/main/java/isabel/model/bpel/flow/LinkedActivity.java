package isabel.model.bpel.flow;

import isabel.model.bpel.OptionalElementNotPresentException;

import java.util.List;

public interface LinkedActivity {
	
	List<SourceElement> getSourceElements() throws OptionalElementNotPresentException;

	List<TargetElement> getTargetElements() throws OptionalElementNotPresentException;
	
}
