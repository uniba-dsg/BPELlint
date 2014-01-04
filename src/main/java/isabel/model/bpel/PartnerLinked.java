package isabel.model.bpel;

import isabel.model.NavigationException;
import isabel.model.Referable;

public interface PartnerLinked extends Referable {

	PartnerLinkElement getPartnerLink() throws NavigationException;

}
