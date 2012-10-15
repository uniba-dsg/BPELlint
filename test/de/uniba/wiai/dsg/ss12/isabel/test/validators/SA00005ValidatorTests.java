package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00005Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00005ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00005Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 5;
	}

	@Override
	public void givenUndefinedTypeInvoke_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Invoke.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Override
	public void givenUndefinedTypeOnEvent_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-OnEvent.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Override
	public void givenUndefinedTypeOnMessage_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-OnMessage.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Override
	public void givenUndefinedTypeReceive_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Receive.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Override
	public void givenUndefinedTypeReply_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Reply.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}
}
