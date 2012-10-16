package de.uniba.wiai.dsg.ss12.isabel.tool.reports;

import java.util.*;

public class IsabelViolationCollector implements ViolationCollector {
	private final List<Violation>	violations	= new ArrayList<>();

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
		if (isValid()){
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
