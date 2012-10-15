package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00010Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00010ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00010Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 10;
	}

	@Override
	public void givenInvokeWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/InvokeWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenOnEventWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/OnEventWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenOnMessageWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/OnMessageWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenReceiveWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/ReceiveWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenReplyWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/ReplyWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenCorrelationSetPropertiesUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/CorrelationSet-Properties-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenFromPropertyUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/From-Property-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Override
	public void givenToPropertyUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/To-Property-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}
}
