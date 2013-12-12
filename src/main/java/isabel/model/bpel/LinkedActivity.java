package isabel.model.bpel;

import java.util.List;

public interface LinkedActivity {
	
	List<SourceElement> getSourceElements() throws OptionalElementNotPresentException;

	List<TargetElement> getTargetElements() throws OptionalElementNotPresentException;
	
}
