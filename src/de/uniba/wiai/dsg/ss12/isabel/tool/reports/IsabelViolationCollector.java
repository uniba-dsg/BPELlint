package de.uniba.wiai.dsg.ss12.isabel.tool.reports;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IsabelViolationCollector implements ViolationCollector {
	private List<Violation>	violations	= new ArrayList<>();

	public void add(Violation violation) {
		violations.add(violation);
	}

	public void add(String bpelFileName, int ruleNumber, int type, int lineNumber, int columnNumber) {
		violations.add(new Violation(bpelFileName, ruleNumber, type, lineNumber, columnNumber));
	}

	@Override
	public List<Violation> getResults() {
		Collections.sort(violations);
		return new ArrayList<>(violations);
	}
}
