package bpellint.core.model.bpel.flow;

import bpellint.core.model.NavigationException;
import bpellint.core.model.Referable;

public interface LinkEntity extends Referable {

	String getLinkName();

	LinkElement getLink() throws NavigationException;

}
