package isabel.tool.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import isabel.tool.ValidationResult;
import isabel.tool.Violation;

public class SimpleValidationResult implements ValidationCollector,
		ValidationResult {

	private final List<Violation> violations = new ArrayList<>();

	public void add(Violation violation) {
		violations.add(violation);
	}

	@Override
	public List<Violation> getViolations() {
		Collections.sort(violations);
		return new ArrayList<>(violations);
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
}
