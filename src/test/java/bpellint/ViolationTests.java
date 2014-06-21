package bpellint;


import org.junit.Test;

import bpellint.tool.validators.result.Violation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ViolationTests {

	@Test
	public void givenViolationA_whenComparedToViolationA_compareToShouldReturnZero()
			throws Exception {
		Violation violation = new Violation("a", 0, 0, 0, 0);

		assertEquals(0, violation.compareTo(violation));
	}

	@Test
	public void givenViolationB_whenComparedToViolationA_compareToShouldReturnZero()
			throws Exception {
		Violation violationA = new Violation("a", 0, 0, 0, 0);
		Violation violationB = new Violation("b", 0, 0, 0, 0);

		assertTrue(0 > violationA.compareTo(violationB));
	}

	@Test
	public void givenDifferentLineViolationA_whenComparedToViolationA_compareToShouldReturnOne()
			throws Exception {
		Violation violation = new Violation("a", 0, 0, 2, 0);
		Violation violationDifferentLine = new Violation("a", 0, 0, 1, 0);

		assertEquals(1, violation.compareTo(violationDifferentLine));
	}

	@Test
	public void givenDifferentColumnViolationA_whenComparedToViolationA_compareToShouldReturnOne()
			throws Exception {
		Violation violation = new Violation("a", 0, 0, 0, 2);
		Violation violationDifferentColumn = new Violation("a", 0, 0, 0, 1);

		assertEquals(1, violation.compareTo(violationDifferentColumn));
	}
}
