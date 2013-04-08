package de.uniba.wiai.dsg.ss12.isabel.tool;

import java.util.List;
import java.util.Set;

public interface ValidationResult {

	public List<Violation> getResults();

	public boolean isValid();

	public Set<Integer> getViolatedRules();
}
