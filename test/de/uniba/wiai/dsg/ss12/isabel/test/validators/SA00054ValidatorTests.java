package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00054Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00054ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00054Validator(bpelProcessFiles, violationCollector);

	}

	@Override
	public int saNumber() {
		return 54;
	}

	@Override
	public void givenEmptyMessageInvokeToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-ToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 54);
	}

	@Override
	public void givenEmptyMessageInvokeToPartsFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 54);
	}

	@Override
	public void givenEmptyMessageReplyToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Reply-ToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 54);
	}
}
