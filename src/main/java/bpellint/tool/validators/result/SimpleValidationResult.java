package bpellint.tool.validators.result;



import java.util.*;

public class SimpleValidationResult implements ValidationCollector,
		ValidationResult {

	private final List<Violation> violations = new ArrayList<>();
	private final List<Warning> warnings = new ArrayList<>();

	@Override
	public void add(Indicator indicator) {
		if (indicator instanceof Violation) {
			violations.add((Violation) indicator);
		}
		if (indicator instanceof Warning) {
			warnings.add((Warning) indicator);
		}
	}

	@Override
	public List<Violation> getViolations() {
		Collections.sort(violations);
		return new ArrayList<>(violations);
	}

	@Override
	public List<Warning> getWarnings() {
		Collections.sort(warnings);
		return new ArrayList<>(warnings);
	}

	@Override
	public boolean isValid() {
		return violations.isEmpty();
	}

	@Override
	public Set<Integer> getViolatedRules() {
		if (isValid()) {
			return Collections.emptySet();
		} else {
			Set<Integer> actualViolatedRules = new HashSet<>();
			for (Violation violation : getViolations()) {
				actualViolatedRules.add(violation.ruleNumber);
			}
			return actualViolatedRules;
		}
	}

	@Override
	public String toString() {
		return "Violated Rules " + getViolatedRules();
	}
}
