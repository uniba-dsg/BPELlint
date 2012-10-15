package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00053Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00053ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00053Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 53;
	}

	@Override
	public void givenEmptyMessageInvokeFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Override
	public void givenEmptyMessageInvokeToPartsFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Override
	public void givenEmptyMessageOnEventFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-OnEvent-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Override
	public void givenEmptyMessageOnMessageFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-OnMessage-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Override
	public void givenEmptyMessageReceiveFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Receive-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}
}
