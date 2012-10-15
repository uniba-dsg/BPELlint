package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00021Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00021ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00021Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 21;
	}

	@Override
	public void givenUndefinedTypeCorrelationSet_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-CorrelationSet.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Override
	public void givenUndefinedTypeFrom_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-From.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Override
	public void givenUndefinedTypeTo_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-To.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

}
