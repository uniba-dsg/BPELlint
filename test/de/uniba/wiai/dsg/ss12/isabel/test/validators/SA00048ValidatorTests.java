package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.IsabelViolationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.SA00048Validator;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public class SA00048ValidatorTests extends ValidatorTests {
	private BpelProcessFiles			files;
	private IsabelViolationCollector	val;
	private SA00048Validator			validator;

	@Override
	public Validator getValidator() {
		return new SA00048Validator(bpelProcessFiles, violationCollector);
	}

	@Override
	public int saNumber() {
		return 48;
	}

	// @Before
	// public void setUp() throws Exception {
	// val = new IsabelViolationCollector();
	// }
	//
	// @Test
	// public void validateInputVariableMessageTypeMessageNotFound() throws
	// Exception {
	// files = new XmlFileLoader()
	// .loadAllProcessFiles("Testcases/SA00048/InputVariable-MessageType-Message-NotFound.bpel");
	// validator = new SA00048Validator(files, val);
	//
	// assertFalse(validator.validate());
	//
	// }
	//
	// @Test
	// public void validateInputVariableTypeMessageOnePartNotFound() throws
	// Exception {
	// files = new XmlFileLoader()
	// .loadAllProcessFiles("Testcases/SA00048/InputVariable-Type-MessageOnePart-NotFound.bpel");
	// validator = new SA00048Validator(files, val);
	//
	// assertFalse(validator.validate());
	// }
	//
	// @Test
	// public void validateInputVariableTypeMessageManyParts() throws Exception
	// {
	// files = new XmlFileLoader()
	// .loadAllProcessFiles("Testcases/SA00048/InputVariable-Type-MessageManyParts.bpel");
	// validator = new SA00048Validator(files, val);
	//
	// assertFalse(validator.validate());
	// }
	//
	// @Test
	// public void validateOutputVariableMessageTypeMessageNotFound() throws
	// Exception {
	// files = new XmlFileLoader()
	// .loadAllProcessFiles("Testcases/SA00048/OutputVariable-MessageType-Message-NotFound.bpel");
	// validator = new SA00048Validator(files, val);
	//
	// assertFalse(validator.validate());
	// }

}
