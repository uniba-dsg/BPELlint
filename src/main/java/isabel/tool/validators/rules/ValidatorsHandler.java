package isabel.tool.validators.rules;

import isabel.io.EnvironmentVariableInterpreter;
import isabel.model.ProcessContainer;
import isabel.tool.ValidationResult;
import isabel.tool.impl.SimpleValidationResult;
import isabel.tool.impl.ValidationCollector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ValidatorsHandler {

	private final ProcessContainer processContainer;
	private final ValidationCollector violationCollector = new SimpleValidationResult();

	public ValidatorsHandler(ProcessContainer processContainer) {
		Objects.requireNonNull(processContainer, "ValidationCollector must not be null");
		this.processContainer = processContainer;
	}

	public ValidationResult validate() {
		List<Integer> rulesToValidate = new EnvironmentVariableInterpreter().getRulesToValidate();
		for (Validator validator : createValidators()) {
			if (rulesToValidate.contains(validator.getSaNumber())) {
				validator.validate();
			}
		}
		return (ValidationResult) violationCollector;
	}

	private List<Validator> createValidators() {

		List<Validator> validators = new ArrayList<>();

		validators.add(new SA00001Validator(processContainer, violationCollector));
		validators.add(new SA00002Validator(processContainer, violationCollector));
		validators.add(new SA00003Validator(processContainer, violationCollector));
		// validators.add(new SA00004Validator(files, violationCollector));
		validators.add(new SA00005Validator(processContainer, violationCollector));
		validators.add(new SA00006Validator(processContainer, violationCollector));
		validators.add(new SA00007Validator(processContainer, violationCollector));
		validators.add(new SA00008Validator(processContainer, violationCollector));
		// validators.add(new SA00009Validator(files, violationCollector));

		validators.add(new SA00010Validator(processContainer, violationCollector));
		validators.add(new SA00011Validator(processContainer, violationCollector));
		validators.add(new SA00012Validator(processContainer, violationCollector));
		validators.add(new SA00013Validator(processContainer, violationCollector));
		validators.add(new SA00014Validator(processContainer, violationCollector));
		validators.add(new SA00015Validator(processContainer, violationCollector));
		validators.add(new SA00016Validator(processContainer, violationCollector));
		validators.add(new SA00017Validator(processContainer, violationCollector));
		validators.add(new SA00018Validator(processContainer, violationCollector));
		validators.add(new SA00019Validator(processContainer, violationCollector));

		validators.add(new SA00020Validator(processContainer, violationCollector));
		validators.add(new SA00021Validator(processContainer, violationCollector));
		validators.add(new SA00022Validator(processContainer, violationCollector));
		validators.add(new SA00023Validator(processContainer, violationCollector));
		validators.add(new SA00024Validator(processContainer, violationCollector));
		validators.add(new SA00025Validator(processContainer, violationCollector));
		// validators.add(new SA00026Validator(files, violationCollector));
		// validators.add(new SA00027Validator(files, violationCollector));
		// validators.add(new SA00028Validator(files, violationCollector));
		// validators.add(new SA00029Validator(files, violationCollector));

		// validators.add(new SA00030Validator(files, violationCollector));
		// validators.add(new SA00031Validator(files, violationCollector));
		validators.add(new SA00032Validator(processContainer, violationCollector));
		// validators.add(new SA00033Validator(files, violationCollector));
		validators.add(new SA00034Validator(processContainer, violationCollector));
		validators.add(new SA00035Validator(processContainer, violationCollector));
		validators.add(new SA00036Validator(processContainer, violationCollector));
		validators.add(new SA00037Validator(processContainer, violationCollector));
		// validators.add(new SA00038Validator(files, violationCollector));
		// validators.add(new SA00039Validator(files, violationCollector));

		// validators.add(new SA00040Validator(files, violationCollector));
		// validators.add(new SA00041Validator(files, violationCollector));
		// validators.add(new SA00042Validator(files, violationCollector));
		// validators.add(new SA00043Validator(files, violationCollector));
		validators.add(new SA00044Validator(processContainer, violationCollector));
		validators.add(new SA00045Validator(processContainer, violationCollector));
		validators.add(new SA00046Validator(processContainer, violationCollector));
		validators.add(new SA00047Validator(processContainer, violationCollector));
		validators.add(new SA00048Validator(processContainer, violationCollector));
		// validators.add(new SA00049Validator(files, violationCollector));

		validators.add(new SA00050Validator(processContainer, violationCollector));
		validators.add(new SA00051Validator(processContainer, violationCollector));
		validators.add(new SA00052Validator(processContainer, violationCollector));
		validators.add(new SA00053Validator(processContainer, violationCollector));
		validators.add(new SA00054Validator(processContainer, violationCollector));
		validators.add(new SA00055Validator(processContainer, violationCollector));
		validators.add(new SA00056Validator(processContainer, violationCollector));
		validators.add(new SA00057Validator(processContainer, violationCollector));
		validators.add(new SA00058Validator(processContainer, violationCollector));
		validators.add(new SA00059Validator(processContainer, violationCollector));

		validators.add(new SA00060Validator(processContainer, violationCollector));
		validators.add(new SA00061Validator(processContainer, violationCollector));
		validators.add(new SA00062Validator(processContainer, violationCollector));
		validators.add(new SA00063Validator(processContainer, violationCollector));
		validators.add(new SA00064Validator(processContainer, violationCollector));
		validators.add(new SA00065Validator(processContainer, violationCollector));
		validators.add(new SA00066Validator(processContainer, violationCollector));
		validators.add(new SA00067Validator(processContainer, violationCollector));
		validators.add(new SA00068Validator(processContainer, violationCollector));
		validators.add(new SA00069Validator(processContainer, violationCollector));

		validators.add(new SA00070Validator(processContainer, violationCollector));
		validators.add(new SA00071Validator(processContainer, violationCollector));
		validators.add(new SA00072Validator(processContainer, violationCollector));
		// validators.add(new SA00073Validator(files, violationCollector));
		// validators.add(new SA00074Validator(files, violationCollector));
		// validators.add(new SA00075Validator(files, violationCollector));
		validators.add(new SA00076Validator(processContainer, violationCollector));
		validators.add(new SA00077Validator(processContainer, violationCollector));
		validators.add(new SA00078Validator(processContainer, violationCollector));
		validators.add(new SA00079Validator(processContainer, violationCollector));

		validators.add(new SA00080Validator(processContainer, violationCollector));
		validators.add(new SA00081Validator(processContainer, violationCollector));
		validators.add(new SA00082Validator(processContainer, violationCollector));
		validators.add(new SA00083Validator(processContainer, violationCollector));
		validators.add(new SA00084Validator(processContainer, violationCollector));
		validators.add(new SA00085Validator(processContainer, violationCollector));
		validators.add(new SA00086Validator(processContainer, violationCollector));
		validators.add(new SA00087Validator(processContainer, violationCollector));
		validators.add(new SA00088Validator(processContainer, violationCollector));
		validators.add(new SA00089Validator(processContainer, violationCollector));

		validators.add(new SA00090Validator(processContainer, violationCollector));
		validators.add(new SA00091Validator(processContainer, violationCollector));
		validators.add(new SA00092Validator(processContainer, violationCollector));
		validators.add(new SA00093Validator(processContainer, violationCollector));
		// validators.add(new SA00094Validator(files, violationCollector));
		validators.add(new SA00095Validator(processContainer, violationCollector));

		return validators;
	}
}
