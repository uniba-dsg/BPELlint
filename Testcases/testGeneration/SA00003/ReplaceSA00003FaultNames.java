import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class ReplaceSA00003FaultNames {

	private static final Path sourcePath = Paths
			.get("./base");
	private static final Path destinationPath = Paths
			.get("./generated");

	private static final String AMBIGUOUS_RECEIVE = "AR:ambiguousReceive";
	private static final String COMPLETION_CONDITION_FAILURE = "CCF:completionConditionFailure";
	private static final String CONFLICTING_RECEIVE = "CRec:conflictingReceive";
	private static final String CONFLICTING_REQUEST = "CReq:conflictingRequest";
	private static final String CORRELATION_VIOLATION = "CV:correlationViolation";
	private static final String INVALID_BRANCH_CONDITION = "IBC:invalidBranchCondition";
	private static final String INVALID_EXPRESSION_VALUE = "IEV:invalidExpressionValue";
	private static final String INVALID_VARIABLES = "IV:invalidVariables";
	private static final String MISMATCHED_ASSIGNMENT_FAILURE = "MAF:mismatchedAssignmentFailure";
	private static final String MISSING_REPLY = "MRep:missingReply";
	private static final String MISSING_REQUEST = "MReq:missingRequest";
	private static final String SCOPEINITIALIZATION_FAILURE = "SIF:scopeInitializationFailure";
	private static final String SELECTION_FAILURE = "SF:selectionFailure";
	private static final String SUBLANGUAGE_EXECUTION_FAULT = "SLEF:subLanguageExecutionFault";
	private static final String UNINITIALIZED_PARTNER_ROLE = "UPR:uninitializedPartnerRole";
	private static final String UNINITIALIZED_VARIABLE = "UV:uninitializedVariable";
	private static final String UNSUPPORTED_REFERENCE = "UR:unsupportedReference";
	private static final String XSLT_INVALID_SOURCE = "XIS:xsltInvalidSource";
	private static final String XSLT_STYLESHEET_NOT_FOUND = "XSNF:xsltStylesheetNotFound";

	private static final List<String> faultNames = Arrays.asList(AMBIGUOUS_RECEIVE,
			COMPLETION_CONDITION_FAILURE, CONFLICTING_RECEIVE, CONFLICTING_REQUEST,
			CORRELATION_VIOLATION, INVALID_BRANCH_CONDITION, INVALID_EXPRESSION_VALUE,
			INVALID_VARIABLES, MISMATCHED_ASSIGNMENT_FAILURE, MISSING_REPLY, MISSING_REQUEST,
			SCOPEINITIALIZATION_FAILURE, SELECTION_FAILURE, SUBLANGUAGE_EXECUTION_FAULT,
			UNINITIALIZED_PARTNER_ROLE, UNINITIALIZED_VARIABLE, UNSUPPORTED_REFERENCE,
			XSLT_INVALID_SOURCE, XSLT_STYLESHEET_NOT_FOUND);

	public static void main(String[] args) {
		if (!sourcePath.toFile().isDirectory()) {
			System.err.println("The sourcePath must be an directory.");
			return;
		}

		try {
			Files.createDirectory(destinationPath);
			for (Path sourceFile : Files.newDirectoryStream(sourcePath)) {
				generateTestForAllFaultNames(sourceFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	static void generateTestForAllFaultNames(Path sourceFile) throws IOException {
		for (String fault : faultNames) {
			String faultAbbreviation = fault.split(":")[0];
			String faultName = fault.split(":")[1];

			Path destinationFilePath = generateDestinationFilePath(sourceFile, faultAbbreviation);
			Files.copy(sourceFile, destinationFilePath);

			replaceFaultNameInDestinationFile(faultName, destinationFilePath);
		}
	}

	private static Path generateDestinationFilePath(Path sourceFile, String postFix) {
		Path sourceFilePath = sourcePath.relativize(sourceFile);
		String nameCopy = destinationPath.resolve(sourceFilePath).toAbsolutePath().toString();
		String endingFreeName = nameCopy.substring(0, nameCopy.length()-5);

		String destinationFileName = endingFreeName + postFix + ".bpel";
		return Paths.get(destinationFileName);
	}

	private static void replaceFaultNameInDestinationFile(String faultName, Path destinationFilePath)
			throws IOException {
		List<String> lines = Files.readAllLines(destinationFilePath);
		List<String> modifiedLines = new LinkedList<>();
		for (String line : lines) {
			modifiedLines.add(modify(line, faultName));
		}

		Files.write(destinationFilePath, modifiedLines);
		System.out.println("Generated " + destinationFilePath);
	}

	private static String modify(String line, String faultName) {
		String faultOccurance = "<catch faultName=\"bpel:completionConditionFailure\">";
		if (faultOccurance.equals(line.trim())) {
			String indentation = line.split(faultOccurance)[0];
			String modifiedLine = indentation + "<catch faultName=\"bpel:" + faultName + "\">";
			return modifiedLine;
		} else {
			return line;
		}
	}

}
