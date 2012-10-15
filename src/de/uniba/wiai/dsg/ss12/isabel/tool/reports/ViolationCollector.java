package de.uniba.wiai.dsg.ss12.isabel.tool.reports;

import java.util.List;

public interface ViolationCollector {

	public void add(Violation violation);

	public List<Violation> getResults();
}
