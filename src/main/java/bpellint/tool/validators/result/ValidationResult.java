package bpellint.tool.validators.result;

import java.util.List;
import java.util.Set;

public interface ValidationResult {

	public List<Violation> getViolations();

	public List<Warning> getWarnings();

	public boolean isValid();

	public Set<Integer> getViolatedRules();
}
