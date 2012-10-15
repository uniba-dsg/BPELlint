package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00013Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00013ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00013Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 13;
	}
}