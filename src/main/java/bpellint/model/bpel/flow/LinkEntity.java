package bpellint.model.bpel.flow;

import bpellint.model.NavigationException;
import bpellint.model.Referable;

public interface LinkEntity extends Referable {

	String getLinkName();

	LinkElement getLink() throws NavigationException;

}
