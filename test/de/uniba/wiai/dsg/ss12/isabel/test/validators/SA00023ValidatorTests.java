package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00023Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00023ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00023Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 23;
	}

}
