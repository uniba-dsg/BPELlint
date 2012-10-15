package de.uniba.wiai.dsg.ss12.isabel.test;

import org.junit.Test;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.IsabelViolationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.Violation;

public class IsabelViolationCollectorTest {

	@Test
	public void testShortDesc() throws ValidationException {
		IsabelViolationCollector val = new IsabelViolationCollector();

		val.add(new Violation("fileXXX222", 1, 2, 180, 60));
		val.add(new Violation("fileXXX4556", 8, 1, 80, 20));
		val.add(new Violation("fileXXX45", 8, 1, 80, 20));
		val.add(new Violation("fileXXX4", 8, 1, 80, 20));
		val.add(new Violation("fileXXX45", 8, 1, 80, 20));
	}

}
