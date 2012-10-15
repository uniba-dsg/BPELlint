package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import org.junit.Test;

import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00011Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00011ValidatorTests extends ValidatorTests {

	@Override
	public Validator getValidator() {
		return new SA00011Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 11;
	}

	@Override
	@Test
	public void givenImportNoNameSpace_whenLoadedAllProcessFiles_validateShouldReturnSA00012Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00012/Import-NoNameSpace.bpel");
		assertionEqualsCorrespondingTo(saNumber() == 11);
	}

}
