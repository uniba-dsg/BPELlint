package isabel.model.bpel;

import isabel.model.NavigationException;
import isabel.model.Referable;

public interface LinkEntity extends Referable {
	
	String getLinkName();
	
	LinkElement getLink()  throws NavigationException;
}
