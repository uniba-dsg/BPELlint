package de.uniba.wiai.dsg.ss12.isabel.tool.reports;

import java.util.List;
import java.util.Set;

public interface ViolationCollector {

	public void add(Violation violation);

	public List<Violation> getResults();

	public boolean isValid();

	public Set<Integer> getViolatedRules();
}
