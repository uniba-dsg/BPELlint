package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ValidationResult;

import java.util.ArrayList;
import java.util.List;

public class ValidatorsHandler {

	private final BpelProcessFiles files;
	private final ValidationResult violationCollector;

	public ValidatorsHandler(BpelProcessFiles files,
	                         ValidationResult violationCollector) {
		if (files == null) {
			throw new IllegalArgumentException(
					"ValidationResult must not be null");
		}
		if (violationCollector == null) {
			throw new IllegalArgumentException("FileHandler must not be null");
		}

		this.files = files;
		this.violationCollector = violationCollector;
	}

	public void validate() {
		List<Validator> validators = createValidators();

		for (Validator validator : validators) {
			validator.validate();
		}
	}

	private List<Validator> createValidators() {

		List<Validator> validators = new ArrayList<>();

		validators.add(new SA00001Validator(files, violationCollector));
		validators.add(new SA00002Validator(files, violationCollector));
		validators.add(new SA00003Validator(files, violationCollector));
		// validators.add(new SA00004Validator(files, violationCollector));
		validators.add(new SA00005Validator(files, violationCollector));
		validators.add(new SA00006Validator(files, violationCollector));
		validators.add(new SA00007Validator(files, violationCollector));
		validators.add(new SA00008Validator(files, violationCollector));
		// validators.add(new SA00009Validator(files, violationCollector));

		validators.add(new SA00010Validator(files, violationCollector));
		validators.add(new SA00011Validator(files, violationCollector));
		validators.add(new SA00012Validator(files, violationCollector));
		validators.add(new SA00013Validator(files, violationCollector));
		// validators.add(new SA00014Validator(files, violationCollector));
		// validators.add(new SA00015Validator(files, violationCollector));
		// validators.add(new SA00016Validator(files, violationCollector));
		// validators.add(new SA00017Validator(files, violationCollector));
		// validators.add(new SA00018Validator(files, violationCollector));
		// validators.add(new SA00019Validator(files, violationCollector));

		validators.add(new SA00020Validator(files, violationCollector));
		validators.add(new SA00021Validator(files, violationCollector));
		validators.add(new SA00022Validator(files, violationCollector));
		validators.add(new SA00023Validator(files, violationCollector));
		validators.add(new SA00024Validator(files, violationCollector));
		validators.add(new SA00025Validator(files, violationCollector));
		// validators.add(new SA00026Validator(files, violationCollector));
		// validators.add(new SA00027Validator(files, violationCollector));
		// validators.add(new SA00028Validator(files, violationCollector));
		// validators.add(new SA00029Validator(files, violationCollector));

		// validators.add(new SA00030Validator(files, violationCollector));
		// validators.add(new SA00031Validator(files, violationCollector));
		// validators.add(new SA00032Validator(files, violationCollector));
		// validators.add(new SA00033Validator(files, violationCollector));
		// validators.add(new SA00034Validator(files, violationCollector));
		// validators.add(new SA00035Validator(files, violationCollector));
		// validators.add(new SA00036Validator(files, violationCollector));
		// validators.add(new SA00037Validator(files, violationCollector));
		// validators.add(new SA00038Validator(files, violationCollector));
		// validators.add(new SA00039Validator(files, violationCollector));

		// validators.add(new SA00040Validator(files, violationCollector));
		// validators.add(new SA00041Validator(files, violationCollector));
		// validators.add(new SA00042Validator(files, violationCollector));
		// validators.add(new SA00043Validator(files, violationCollector));
		validators.add(new SA00044Validator(files, violationCollector));
		validators.add(new SA00045Validator(files, violationCollector));
		validators.add(new SA00046Validator(files, violationCollector));
		validators.add(new SA00047Validator(files, violationCollector));
		validators.add(new SA00048Validator(files, violationCollector));
		// validators.add(new SA00049Validator(files, violationCollector));

		validators.add(new SA00050Validator(files, violationCollector));
		validators.add(new SA00051Validator(files, violationCollector));
		validators.add(new SA00052Validator(files, violationCollector));
		validators.add(new SA00053Validator(files, violationCollector));
		validators.add(new SA00054Validator(files, violationCollector));
		validators.add(new SA00055Validator(files, violationCollector));
		// validators.add(new SA00056Validator(files, violationCollector));
		// validators.add(new SA00057Validator(files, violationCollector));
		// validators.add(new SA00058Validator(files, violationCollector));
		validators.add(new SA00059Validator(files, violationCollector));

		// validators.add(new SA00060Validator(files, violationCollector));
		// validators.add(new SA00061Validator(files, violationCollector));
		// validators.add(new SA00062Validator(files, violationCollector));
		validators.add(new SA00063Validator(files, violationCollector));
		// validators.add(new SA00064Validator(files, violationCollector));
		// validators.add(new SA00065Validator(files, violationCollector));
		// validators.add(new SA00066Validator(files, violationCollector));
		// validators.add(new SA00067Validator(files, violationCollector));
		// validators.add(new SA00068Validator(files, violationCollector));
		// validators.add(new SA00069Validator(files, violationCollector));

		// validators.add(new SA00070Validator(files, violationCollector));
		// validators.add(new SA00071Validator(files, violationCollector));
		// validators.add(new SA00072Validator(files, violationCollector));
		// validators.add(new SA00073Validator(files, violationCollector));
		// validators.add(new SA00074Validator(files, violationCollector));
		// validators.add(new SA00075Validator(files, violationCollector));
		// validators.add(new SA00076Validator(files, violationCollector));
		// validators.add(new SA00077Validator(files, violationCollector));
		// validators.add(new SA00078Validator(files, violationCollector));
		// validators.add(new SA00079Validator(files, violationCollector));

		// validators.add(new SA00080Validator(files, violationCollector));
		// validators.add(new SA00081Validator(files, violationCollector));
		// validators.add(new SA00082Validator(files, violationCollector));
		// validators.add(new SA00083Validator(files, violationCollector));
		// validators.add(new SA00084Validator(files, violationCollector));
		// validators.add(new SA00085Validator(files, violationCollector));
		// validators.add(new SA00086Validator(files, violationCollector));
		// validators.add(new SA00087Validator(files, violationCollector));
		// validators.add(new SA00088Validator(files, violationCollector));
		// validators.add(new SA00089Validator(files, violationCollector));

		// validators.add(new SA00090Validator(files, violationCollector));
		// validators.add(new SA00091Validator(files, violationCollector));
		// validators.add(new SA00092Validator(files, violationCollector));
		// validators.add(new SA00093Validator(files, violationCollector));
		// validators.add(new SA00094Validator(files, violationCollector));
		// validators.add(new SA00095Validator(files, violationCollector));

		return validators;
	}
}
