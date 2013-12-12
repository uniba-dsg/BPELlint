package isabel.model.bpel;

import isabel.model.NavigationException;

public interface LinkEntity {
	
	String getLinkName();
	
	LinkElement getLink()  throws NavigationException;
}
