package bpellint.model.bpel.flow;


import java.util.List;

import bpellint.model.Referable;

public interface LinkEntityContainer extends Referable {

	List<LinkEntity> getAll();

}
