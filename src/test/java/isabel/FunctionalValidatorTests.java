package isabel;

import isabel.io.ValidationResultPrinter;
import isabel.io.VerbosityLevel;
import isabel.tool.Isabel;
import isabel.tool.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

/**
 * Executes tests based on the files from within the /Testcases directory.
 */
@RunWith(value = Parameterized.class)
public class FunctionalValidatorTests {

	private final Set<Integer> violatedRules;
	private final String bpel;

	public FunctionalValidatorTests(String bpel, String violatedRules) {
		this.violatedRules = parseString(violatedRules);
		this.bpel = bpel;
	}

	private Set<Integer> parseString(String violatedRules) {
		String[] elements = violatedRules.split(",");
		Set<Integer> parsedElements = new HashSet<>();
		for (String element : elements) {
			if (element == null || element.isEmpty()) {
				continue;
			}
			parsedElements.add(Integer.parseInt(element.trim()));
		}
		return parsedElements;
	}

	@Parameterized.Parameters(name = "{index}: {0} violates {1}")
	public static Collection<Object[]> data() throws IOException {
		List<Object[]> bpelFiles = new LinkedList<>();
		bpelFiles.addAll(saViolationTests());
		bpelFiles.addAll(new HappyPathTests(Paths.get("Testcases/betsy")).list());
        bpelFiles.addAll(new HappyPathTests(Paths.get("Testcases/calculator")).list());
        bpelFiles.addAll(new HappyPathTests(Paths.get("Testcases/complex")).list());

		return bpelFiles;
	}

	public static List<Object[]> saViolationTests() {
		Object[][] data = new Object[][]{

				{"Testcases/rules/SA00001/Notification.bpel", "1"},
				{"Testcases/rules/SA00001/SolicitResponse.bpel", "1"},

				{"Testcases/rules/SA00002/OverloadedOperationNames.bpel", "2"},

				{"Testcases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInDirectFaultHandlers.bpel", "3"},
				{"Testcases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInDirectFaultHandlersInProcess.bpel", "3"},
				{"Testcases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInIndirectFaultHandlers.bpel", "3"},
				{"Testcases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInIndirectNestedFaultHandlers.bpel", "3"},
				{"Testcases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInNestedFaultHandlers.bpel", "3"},

				{"Testcases/rules/SA00005/InvokeWithNonExistentPortType.bpel", "5, 10"},
				{"Testcases/rules/SA00005/OnEventWithNonExistentPortType.bpel", "5, 10"},
				{"Testcases/rules/SA00005/OnMessageWithNonExistentPortType.bpel", "5, 10"},
				{"Testcases/rules/SA00005/ReceiveWithNonExistentPortType.bpel", "5, 10"},
				{"Testcases/rules/SA00005/ReplyWithNonExistentPortType.bpel", "5, 10"},

				{"Testcases/rules/SA00006/RethrowOutsideFaultHandlers.bpel", "6"},

				{"Testcases/rules/SA00007/CompensateScopeOutsideFaultHandlers.bpel", "7"},

				{"Testcases/rules/SA00008/CompensateOutsideFaultHandlers.bpel", "8"},

				{"Testcases/rules/SA00010/UndefinedType-Catch-FaultElement.bpel", "10"},
				{"Testcases/rules/SA00010/UndefinedType-Catch-FaultMessageType.bpel", "10"},
				{"Testcases/rules/SA00010/UndefinedType-CorrelationSet.bpel", "10, 21"},
				{"Testcases/rules/SA00010/UndefinedType-From.bpel", "10, 21"},
				{"Testcases/rules/SA00010/UndefinedType-Invoke.bpel", "10, 5"},
				{"Testcases/rules/SA00010/UndefinedType-OnEvent.bpel", "10, 5"},
				{"Testcases/rules/SA00010/UndefinedType-OnMessage.bpel", "10, 5"},
				{"Testcases/rules/SA00010/UndefinedType-PartnerLink.bpel", "10"},
				{"Testcases/rules/SA00010/UndefinedType-Receive.bpel", "10, 5"},
				{"Testcases/rules/SA00010/UndefinedType-Reply.bpel", "10, 5"},
				{"Testcases/rules/SA00010/UndefinedType-To.bpel", "10, 21"},
				{"Testcases/rules/SA00010/UndefinedType-Variable-Element.bpel", "10"},
				{"Testcases/rules/SA00010/UndefinedType-Variable-MessageType.bpel", "10"},
				{"Testcases/rules/SA00010/UndefinedType-Variable-Type.bpel", "10"},

				{"Testcases/rules/SA00011/Import-WrongNameSpace.bpel", "11"},

				{"Testcases/rules/SA00012/Import-NoNameSpace.bpel", "12, 11"},

				{"Testcases/rules/SA00013/Import-WrongImportType.bpel", "13"},

				{"Testcases/rules/SA00015/NoActivityWithCreateInstanceSetToYes.bpel", "15"},
				{"Testcases/rules/SA00015/OnlyActivityWithCreateInstanceSetToNo.bpel", "15"},

				{"Testcases/rules/SA00016/PartnerLinkWithoutMyRoleAndPartnerRole.bpel", "16"},

				{"Testcases/rules/SA00017/InitializePartnerRoleUsedOnPartnerLinkWithoutPartnerRole.bpel", "17"},

				{"Testcases/rules/SA00018/TwoPartnerLinksWithSameName.bpel", "18"},

				{"Testcases/rules/SA00019/PropertyWithoutTypeOrElement.bpel", "19"},
				{"Testcases/rules/SA00019/PropertyWithTypeAndElement.bpel", "19"},

				{"Testcases/rules/SA00020/PropertyAlias-AllOptionalAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypeAttribute.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypeElementAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypePartElementAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypePartTypeAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypeTypeAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-MessageTypeTypeElementAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-NoOptionalAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-PartAttribute.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-PartElementAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-PartTypeAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-PartTypeElementAttributes.bpel", "20"},
				{"Testcases/rules/SA00020/PropertyAlias-TypeElementAttributes.bpel", "20"},

				{"Testcases/rules/SA00021/CorrelationSet-Properties-Undefined.bpel", "21, 10"},
				{"Testcases/rules/SA00021/From-Property-Undefined.bpel", "21, 10"},
				{"Testcases/rules/SA00021/OnEvent-Variable-Undefined.bpel", "21"},
				{"Testcases/rules/SA00021/To-Property-Undefined.bpel", "21, 10"},

				{"Testcases/rules/SA00022/Duplicate-propertyAliasElement.bpel", "22"},
				{"Testcases/rules/SA00022/Duplicate-propertyAliasMessageType.bpel", "22"},
				{"Testcases/rules/SA00022/Duplicate-propertyAliasType.bpel", "22"},

				{"Testcases/rules/SA00023/Process-Duplicated-Variables.bpel", "23"},
				{"Testcases/rules/SA00023/Scope-Duplicated-Variables.bpel", "23"},
				{"Testcases/rules/SA00023/Scope-Scope-Duplicated-Variables.bpel", "23"},

				{"Testcases/rules/SA00025/Variable-havingMessageTypeAndElement.bpel", "25"},
				{"Testcases/rules/SA00025/Variable-havingTypeAndElement.bpel", "25"},
				{"Testcases/rules/SA00025/Variable-havingTypeAndMessageType.bpel", "25"},
				{"Testcases/rules/SA00025/Variable-havingTypeAndMessageTypeAndElement.bpel", "25"},
				{"Testcases/rules/SA00025/Variable-missingMessageTypeAndTypeAndElement.bpel", "25"},

				{"Testcases/rules/SA00032/ToMessageTypeVariableSuperfluousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariableSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/FromMessageTypeVariableSuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariableSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/ToPartnerLinkSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/ToPartnerLinkSuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariableQuerySuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariableQuerySuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromPartnerLinkSuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromPartnerLinkSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/FromPartnerLinkMissingEndpointReferenceAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariablePropertySuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariablePropertySuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/FromExpressionLanguageSuperfliciousAttributes.bpel", "32"},
				{"Testcases/rules/SA00032/ToExpressionLanguageSuperfliciousAttributes.bpel", "32"},
				{"Testcases/rules/SA00032/FromExpressionLanguageSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/ToExpressionLanguageSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/FromLiteralSuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromLiteralSuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariablePropertySuperfliciousAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariablePropertySuperfliciousChild.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariableQueryAdditionalAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/FromVariableQueryAdditionalChild.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariableQueryAdditionalAttribute.bpel", "32"},
				{"Testcases/rules/SA00032/ToVariableQueryAdditionalChild.bpel", "32"},

				{"Testcases/rules/SA00034/FromElementVariablePartAttribute.bpel", "34"},
				{"Testcases/rules/SA00034/FromOnEventElementVariablePartAttribute.bpel", "34"},
				{"Testcases/rules/SA00034/FromTypeVariablePartAttribute.bpel", "34"},
				{"Testcases/rules/SA00034/ToElementVariablePartAttribute.bpel", "34"},
				{"Testcases/rules/SA00034/ToOnEventElementVariablePartAttribute.bpel", "34"},
				{"Testcases/rules/SA00034/ToTypeVariablePartAttribute.bpel", "34"},

				{"Testcases/rules/SA00035/FromLinkTypeMyRolePartnerLinkWithoutMyRole.bpel", "35"},

				{"Testcases/rules/SA00036/FromPartnerRoleWithoutPartnerRolePartnerLink.bpel", "36"},

				{"Testcases/rules/SA00037/ToLinkTypeWithoutPartnerRolePartnerLink.bpel", "37"},

				{"Testcases/rules/SA00044/Process-CorrelationSet-Ambiguous.bpel", "44"},
				{"Testcases/rules/SA00044/Scope-CorrelationSets-Ambiguous.bpel", "44"},

				{"Testcases/rules/SA00045/Property-TypeComplexType.bpel", "45"},
				{"Testcases/rules/SA00045/Property-TypeMissing.bpel", "45, 19"},

				{"Testcases/rules/SA00046/Invoke-OneWay-Correlation-Pattern.bpel", "46"},
				{"Testcases/rules/SA00046/Invoke-RequestResponse-Correlation-PatternMissing.bpel", "46"},

				{"Testcases/rules/SA00047/EmptyMessage-Invoke-FromParts.bpel", "47, 53"},
				{"Testcases/rules/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel", "47, 54, 53"},
				{"Testcases/rules/SA00047/EmptyMessage-Invoke-ToParts.bpel", "47, 54"},
				{"Testcases/rules/SA00047/EmptyMessage-OnEvent-FromParts.bpel", "47, 53"},
				{"Testcases/rules/SA00047/EmptyMessage-OnMessage-FromParts.bpel", "47, 53"},
				{"Testcases/rules/SA00047/EmptyMessage-Receive-FromParts.bpel", "47, 53"},
				{"Testcases/rules/SA00047/EmptyMessage-Reply-ToParts.bpel", "47, 54"},
				{"Testcases/rules/SA00047/Invoke-OneWay-NoInputVariable-NoToParts.bpel", "47"},
				{"Testcases/rules/SA00047/Invoke-RequestResponse-NoInputOutputVariables-NoToFromParts.bpel", "47"},
				{"Testcases/rules/SA00047/Invoke-RequestResponse-NoInputVariable-NoToParts.bpel", "47"},
				{"Testcases/rules/SA00047/Invoke-RequestResponse-NoOutputVariable-NoFromParts.bpel", "47"},
				{"Testcases/rules/SA00047/NoVariable-NoFromPart-OnEvent.bpel", "47"},
				{"Testcases/rules/SA00047/NoVariable-NoFromPart-OnMessage.bpel", "47"},
				{"Testcases/rules/SA00047/NoVariable-NoFromPart-Receive.bpel", "47"},
				{"Testcases/rules/SA00047/NoVariable-NoToPart-NoFromPart-ReceiveReply.bpel", "47"},
				{"Testcases/rules/SA00047/NoVariable-NoToPart-Reply.bpel", "47"},

				{"Testcases/rules/SA00048/InputOutputVariable-Message-NotFound.bpel", "48"},
				{"Testcases/rules/SA00048/InputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"Testcases/rules/SA00048/InputVariable-Type-MessageManyParts.bpel", "48"},
				{"Testcases/rules/SA00048/InputVariable-Type-MessageOnePart-NotFound.bpel", "48"},
				{"Testcases/rules/SA00048/OutputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"Testcases/rules/SA00048/OutputVariable-Type-MessageManyParts.bpel", "48"},
				{"Testcases/rules/SA00048/OutputVariable-Type-MessageOnePart-NotFound.bpel", "48"},

				{"Testcases/rules/SA00050/Invoke-MissingToPart.bpel", "50"},
				{"Testcases/rules/SA00050/Receive-MissingToPart.bpel", "50"},

				{"Testcases/rules/SA00051/Invoke-ToPartsAndInputVariable.bpel", "51"},

				{"Testcases/rules/SA00052/Invoke-FromPartsAndOutputVariable.bpel", "52"},

				{"Testcases/rules/SA00053/Invoke-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"Testcases/rules/SA00053/OnEvent-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"Testcases/rules/SA00053/OnMessage-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"Testcases/rules/SA00053/Receive-FromPartDifferingFromMessageDefinition.bpel", "53"},

				{"Testcases/rules/SA00054/Invoke-ToPartDifferingFromMessageDefinition.bpel", "54"},
				{"Testcases/rules/SA00054/Reply-ToPartDifferingFromMessageDefinition.bpel", "54"},

				{"Testcases/rules/SA00055/Receive-WithFromPartElementAndVariableAttribute.bpel", "55"},

				{"Testcases/rules/SA00057/OnMessageCorrelationYesAndJoin.bpel", "57"},
				{"Testcases/rules/SA00057/OnMessageReceiveCorrelationYesAndJoin.bpel", "57"},
				{"Testcases/rules/SA00057/ReceiveCorrelationYesAndJoin.bpel", "57"},

				{"Testcases/rules/SA00059/Reply-WithToPartElementAndVariableAttribute.bpel", "59"},

				{"Testcases/rules/SA00061/OnEventMessageExchangeNotInScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnEventNoMessageExchangeInScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnEventReplyMessageExchangeNotInScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnEventMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnEventNoMessageExchangeInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnEventReplyMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageMessageExchangeOutOfScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageNoMessageExchangeInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageReplyMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageMessageExchangeNotInScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageNoMessageExchangeInScope.bpel", "61"},
				{"Testcases/rules/SA00061/OnMessageReplyMessageExchangeNotInScope.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveMessageExchangeNotInScope.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveMessageExchangeOutOfScope.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveNoMessageExchangeInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveNoMessageExchangeInScope.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveReplyMessageExchangeNotInProcess.bpel", "61"},
				{"Testcases/rules/SA00061/ReceiveReplyMessageExchangeNotInScope.bpel", "61"},

				{"Testcases/rules/SA00062/Pick-CreateInstanceWithOnAlarm.bpel", "62"},

				{"Testcases/rules/SA00063/OnMessage-With-FromPartAndAttributeVariable.bpel", "63"},

				{"Testcases/rules/SA00064/LinkNameDuplicate.bpel", "64"},

				{"Testcases/rules/SA00065/NoLink.bpel", "65"},
				{"Testcases/rules/SA00065/SourceLinkIsMissing.bpel", "65, 66"},
				{"Testcases/rules/SA00065/SourceLinkOutOfFlow.bpel", "65, 66"},
				{"Testcases/rules/SA00065/TargetLinkIsMissing.bpel", "65, 66"},
				{"Testcases/rules/SA00065/TargetLinkOutOfFlow.bpel", "65, 66"},

				{"Testcases/rules/SA00066/LinkTwoSources.bpel", "66"},
				{"Testcases/rules/SA00066/LinkTwoTargets.bpel", "66"},
				{"Testcases/rules/SA00066/LinkNoSource.bpel", "66"},
				{"Testcases/rules/SA00066/LinkNoTarget.bpel", "66"},

				{"Testcases/rules/SA00067/DoubleLink.bpel", "67"},

				{"Testcases/rules/SA00068/LinkSourceDuplicate.bpel", "68"},

				{"Testcases/rules/SA00069/LinkTargetDuplicate.bpel", "69"},

				{"Testcases/rules/SA00072/FlowSelfLinked.bpel", "72"},
				{"Testcases/rules/SA00072/FlowCyclic.bpel", "72"},
				{"Testcases/rules/SA00072/FlowCyclicSubGraph.bpel", "72"},

				{"Testcases/rules/SA00076/ForEach-DuplicateCounterVariable.bpel", "76"},

				{"Testcases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},
				{"Testcases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},
				{"Testcases/rules/SA00080/EmptyFaultHandlersInScope.bpel", "80"},

				{"Testcases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},
				{"Testcases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},
				{"Testcases/rules/SA00083/EmptyEventHandlersInScope.bpel", "83"},

		};
		return Arrays.asList(data);
	}

	@Test
	public void testValidators() throws Exception {
		ValidationResult validationResult = new Isabel().validate(Paths.get(bpel));

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,
				validationResult);
		String data = "\n" + baos.toString() + "\n";

		assertEquals("BPEL: " + bpel + data, violatedRules,
				validationResult.getViolatedRules());
	}

}
