package de.uniba.wiai.dsg.ss12.isabel.tool.impl;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.Violation;

import java.util.*;

public class SimpleValidationResult implements ValidationCollector, ValidationResult {

	private final List<Violation> violations = new ArrayList<>();

	public void add(Violation violation) {
		violations.add(violation);
	}

	@Override
	public List<Violation> getResults() {
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
			for (Violation violation : getResults()) {
				actualViolatedRules.add(violation.ruleNumber);
			}
			return actualViolatedRules;
		}
	}
}
