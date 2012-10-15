package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00003Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00003ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00003Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 3;
	}
}