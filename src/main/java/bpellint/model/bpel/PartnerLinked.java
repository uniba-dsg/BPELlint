package bpellint.model.bpel;

import bpellint.model.NavigationException;
import bpellint.model.Referable;

public interface PartnerLinked extends Referable {

	PartnerLinkElement getPartnerLink() throws NavigationException;

}
