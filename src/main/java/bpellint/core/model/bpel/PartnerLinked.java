package bpellint.core.model.bpel;

import bpellint.core.model.NavigationException;
import bpellint.core.model.Referable;

public interface PartnerLinked extends Referable {

	PartnerLinkElement getPartnerLink() throws NavigationException;

}
