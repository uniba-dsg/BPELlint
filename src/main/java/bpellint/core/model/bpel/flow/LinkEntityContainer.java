package bpellint.core.model.bpel.flow;


import java.util.List;

import bpellint.core.model.Referable;

public interface LinkEntityContainer extends Referable {

	List<LinkEntity> getAll();

}
