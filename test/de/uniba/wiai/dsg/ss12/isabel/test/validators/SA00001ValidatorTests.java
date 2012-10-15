package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00001Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00001ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00001Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 1;
	}
}