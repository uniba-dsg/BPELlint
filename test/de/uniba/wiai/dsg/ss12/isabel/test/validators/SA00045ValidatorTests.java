package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00045Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00045ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00045Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 45;
	}
}