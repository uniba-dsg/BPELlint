package de.uniba.wiai.dsg.ss12.isabel.test;

import de.uniba.wiai.dsg.ss12.isabel.io.ValidationResultPrinter;
import de.uniba.wiai.dsg.ss12.isabel.io.VerbosityLevel;
import de.uniba.wiai.dsg.ss12.isabel.tool.Isabel;
import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationResult;
import de.uniba.wiai.dsg.ss12.isabel.tool.Violation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
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

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][]{
				// SA violation tests
				{"TestCases/rules/SA00001/Notification.bpel", "1"},
				{"TestCases/rules/SA00001/SolicitResponse.bpel", "1"},

				{"TestCases/rules/SA00002/OverloadedOperationNames.bpel", "2"},

				{"TestCases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInDirectFaultHandlers.bpel", "3"},
				{"TestCases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInDirectFaultHandlersInProcess.bpel", "3"},
				{"TestCases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInIndirectFaultHandlers.bpel", "3"},
				{"TestCases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInIndirectNestedFaultHandlers.bpel", "3"},
				{"TestCases/rules/SA00003/ExitOnStandardFaultButCatchingStandardFaultInNestedFaultHandlers.bpel", "3"},

				{"TestCases/rules/SA00005/InvokeWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/rules/SA00005/OnEventWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/rules/SA00005/OnMessageWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/rules/SA00005/ReceiveWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/rules/SA00005/ReplyWithNonExistentPortType.bpel", "5, 10"},

				{"TestCases/rules/SA00006/RethrowOutsideFaultHandlers.bpel", "6"},

				{"TestCases/rules/SA00007/CompensateScopeOutsideFaultHandlers.bpel", "7"},

				{"TestCases/rules/SA00008/CompensateOutsideFaultHandlers.bpel", "8"},

				{"TestCases/rules/SA00010/UndefinedType-Catch-FaultElement.bpel", "10"},
				{"TestCases/rules/SA00010/UndefinedType-Catch-FaultMessageType.bpel", "10"},
				{"TestCases/rules/SA00010/UndefinedType-CorrelationSet.bpel", "10, 21"},
				{"TestCases/rules/SA00010/UndefinedType-From.bpel", "10, 21"},
				{"TestCases/rules/SA00010/UndefinedType-Invoke.bpel", "10, 5"},
				{"TestCases/rules/SA00010/UndefinedType-OnEvent.bpel", "10, 5"},
				{"TestCases/rules/SA00010/UndefinedType-OnMessage.bpel", "10, 5"},
				{"TestCases/rules/SA00010/UndefinedType-PartnerLink.bpel", "10"},
				{"TestCases/rules/SA00010/UndefinedType-Receive.bpel", "10, 5"},
				{"TestCases/rules/SA00010/UndefinedType-Reply.bpel", "10, 5"},
				{"TestCases/rules/SA00010/UndefinedType-To.bpel", "10, 21"},
				{"TestCases/rules/SA00010/UndefinedType-Variable-Element.bpel", "10"},
				{"TestCases/rules/SA00010/UndefinedType-Variable-MessageType.bpel", "10"},
				{"TestCases/rules/SA00010/UndefinedType-Variable-Type.bpel", "10"},

				{"TestCases/rules/SA00011/Import-WrongNameSpace.bpel", "11"},

				{"TestCases/rules/SA00012/Import-NoNameSpace.bpel", "12, 11"},

				{"TestCases/rules/SA00013/Import-WrongImportType.bpel", "13"},

                {"TestCases/rules/SA00015/NoActivityWithCreateInstanceSetToYes.bpel", "15"},
                {"TestCases/rules/SA00015/OnlyActivityWithCreateInstanceSetToNo.bpel", "15"},

                {"TestCases/rules/SA00016/PartnerLinkWithoutMyRoleAndPartnerRole.bpel","16"},

                {"TestCases/rules/SA00017/InitializePartnerRoleUsedOnPartnerLinkWithoutPartnerRole.bpel","17"},

                {"TestCases/rules/SA00018/TwoPartnerLinksWithSameName.bpel","18"},

                {"TestCases/rules/SA00019/PropertyWithoutTypeOrElement.bpel","19"},
                {"TestCases/rules/SA00019/PropertyWithTypeAndElement.bpel","19"},

				{"TestCases/rules/SA00020/PropertyAlias-AllOptionalAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypeAttribute.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypeElementAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypePartElementAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypePartTypeAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypeTypeAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-MessageTypeTypeElementAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-NoOptionalAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-PartAttribute.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-PartElementAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-PartTypeAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-PartTypeElementAttributes.bpel", "20"},
				{"TestCases/rules/SA00020/PropertyAlias-TypeElementAttributes.bpel", "20"},

				{"TestCases/rules/SA00021/CorrelationSet-Properties-Undefined.bpel", "21, 10"},
				{"TestCases/rules/SA00021/From-Property-Undefined.bpel", "21, 10"},
				{"TestCases/rules/SA00021/OnEvent-Variable-Undefined.bpel", "21"},
				{"TestCases/rules/SA00021/To-Property-Undefined.bpel", "21, 10"},

				{"TestCases/rules/SA00022/Duplicate-propertyAliasElement.bpel", "22"},
				{"TestCases/rules/SA00022/Duplicate-propertyAliasMessageType.bpel", "22"},
				{"TestCases/rules/SA00022/Duplicate-propertyAliasType.bpel", "22"},

				{"TestCases/rules/SA00023/Process-Duplicated-Variables.bpel", "23"},
				{"TestCases/rules/SA00023/Scope-Duplicated-Variables.bpel", "23"},
				{"TestCases/rules/SA00023/Scope-Scope-Duplicated-Variables.bpel", "23"},

				{"TestCases/rules/SA00024/OnEvent-containing-dot.bpel", "24"},
				{"TestCases/rules/SA00024/Variable-containing-dot.bpel", "24"},

				{"TestCases/rules/SA00025/Variable-havingMessageTypeAndElement.bpel", "25"},
				{"TestCases/rules/SA00025/Variable-havingTypeAndElement.bpel", "25"},
				{"TestCases/rules/SA00025/Variable-havingTypeAndMessageType.bpel", "25"},
				{"TestCases/rules/SA00025/Variable-havingTypeAndMessageTypeAndElement.bpel", "25"},
				{"TestCases/rules/SA00025/Variable-missingMessageTypeAndTypeAndElement.bpel", "25"},

				{"TestCases/rules/SA00044/Process-CorrelationSet-Ambiguous.bpel", "44"},
				{"TestCases/rules/SA00044/Scope-CorrelationSets-Ambiguous.bpel", "44"},

				{"TestCases/rules/SA00045/Property-TypeComplexType.bpel", "45"},
				{"TestCases/rules/SA00045/Property-TypeMissing.bpel", "45, 19"},

				{"TestCases/rules/SA00046/Invoke-OneWay-Correlation-Pattern.bpel", "46"},
				{"TestCases/rules/SA00046/Invoke-RequestResponse-Correlation-PatternMissing.bpel", "46"},

				{"TestCases/rules/SA00047/EmptyMessage-Invoke-FromParts.bpel", "47, 53"},
				{"TestCases/rules/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel", "47, 54, 53"},
				{"TestCases/rules/SA00047/EmptyMessage-Invoke-ToParts.bpel", "47, 54"},
				{"TestCases/rules/SA00047/EmptyMessage-OnEvent-FromParts.bpel", "47, 53"},
				{"TestCases/rules/SA00047/EmptyMessage-OnMessage-FromParts.bpel", "47, 53"},
				{"TestCases/rules/SA00047/EmptyMessage-Receive-FromParts.bpel", "47, 53"},
				{"TestCases/rules/SA00047/EmptyMessage-Reply-ToParts.bpel", "47, 54"},
				{"TestCases/rules/SA00047/Invoke-OneWay-NoInputVariable-NoToParts.bpel", "47"},
				{"TestCases/rules/SA00047/Invoke-RequestResponse-NoInputOutputVariables-NoToFromParts.bpel", "47"},
				{"TestCases/rules/SA00047/Invoke-RequestResponse-NoInputVariable-NoToParts.bpel", "47"},
				{"TestCases/rules/SA00047/Invoke-RequestResponse-NoOutputVariable-NoFromParts.bpel", "47"},
				{"TestCases/rules/SA00047/NoVariable-NoFromPart-OnEvent.bpel", "47"},
				{"TestCases/rules/SA00047/NoVariable-NoFromPart-OnMessage.bpel", "47"},
				{"TestCases/rules/SA00047/NoVariable-NoFromPart-Receive.bpel", "47"},
				{"TestCases/rules/SA00047/NoVariable-NoToPart-NoFromPart-ReceiveReply.bpel", "47"},
				{"TestCases/rules/SA00047/NoVariable-NoToPart-Reply.bpel", "47"},

				{"TestCases/rules/SA00048/InputOutputVariable-Message-NotFound.bpel", "48"},
				{"TestCases/rules/SA00048/InputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"TestCases/rules/SA00048/InputVariable-Type-MessageManyParts.bpel", "48"},
				{"TestCases/rules/SA00048/InputVariable-Type-MessageOnePart-NotFound.bpel", "48"},
				{"TestCases/rules/SA00048/OutputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"TestCases/rules/SA00048/OutputVariable-Type-MessageManyParts.bpel", "48"},
				{"TestCases/rules/SA00048/OutputVariable-Type-MessageOnePart-NotFound.bpel", "48"},

				{"TestCases/rules/SA00050/Invoke-MissingToPart.bpel", "50"},
				{"TestCases/rules/SA00050/Receive-MissingToPart.bpel", "50"},

				{"TestCases/rules/SA00051/Invoke-ToPartsAndInputVariable.bpel", "51"},

				{"TestCases/rules/SA00052/Invoke-FromPartsAndOutputVariable.bpel", "52"},

				{"TestCases/rules/SA00053/Invoke-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/rules/SA00053/OnEvent-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/rules/SA00053/OnMessage-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/rules/SA00053/Receive-FromPartDifferingFromMessageDefinition.bpel", "53"},

				{"TestCases/rules/SA00054/Invoke-ToPartDifferingFromMessageDefinition.bpel", "54"},
				{"TestCases/rules/SA00054/Reply-ToPartDifferingFromMessageDefinition.bpel", "54"},

				{"TestCases/rules/SA00055/Receive-WithFromPartElementAndVariableAttribute.bpel", "55"},

				{"TestCases/rules/SA00059/Reply-WithToPartElementAndVariableAttribute.bpel", "59"},

                {"TestCases/rules/SA00062/Pick-CreateInstanceWithOnAlarm.bpel", "62"},

				{"TestCases/rules/SA00063/OnMessage-With-FromPartAndAttributeVariable.bpel", "63"},

                {"TestCases/rules/SA00076/ForEach-DuplicateCounterVariable.bpel", "76"},

                {"TestCases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},
                {"TestCases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},

                {"TestCases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},
                {"TestCases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},

				// betsy tests
				{"TestCases/betsy/basic-activities/Assign-Copy-DoXslTransform-InvalidSourceFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-DoXslTransform-SubLanguageExecutionFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-DoXslTransform-XsltStylesheetNotFound.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-DoXslTransform.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-GetVariableProperty.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-IgnoreMissingFromData.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-KeepSrcElementName.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Copy-Query.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Empty.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Expression-From.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Expression-To.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Literal.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-PartnerLink-UnsupportedReference.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-PartnerLink.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Property.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-SelectionFailure.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-Validate.bpel", ""},
				{"TestCases/betsy/basic-activities/Assign-VariablesUnchangedInspiteOfFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Empty.bpel", ""},
				{"TestCases/betsy/basic-activities/Exit.bpel", ""},
				{"TestCases/betsy/basic-activities/ExtensionActivity-MustUnderstand.bpel", ""},
				{"TestCases/betsy/basic-activities/ExtensionActivity-NoMustUnderstand.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Async.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Catch.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-CatchAll.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-CompensationHandler.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Correlation-Pattern-InitAsync.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Correlation-Pattern-InitSync.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Empty.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-FromParts.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Sync-Fault.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-Sync.bpel", ""},
				{"TestCases/betsy/basic-activities/Invoke-ToParts.bpel", ""},
				{"TestCases/betsy/basic-activities/Receive-AmbiguousReceiveFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Receive-ConflictingReceiveFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Receive-Correlation-InitAsync.bpel", ""},
				{"TestCases/betsy/basic-activities/Receive-Correlation-InitSync.bpel", ""},
				{"TestCases/betsy/basic-activities/Receive.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-ConflictingRequestFault.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-Correlation-InitAsync.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-Correlation-InitSync.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-CorrelationViolation-Join.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-CorrelationViolation-No.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-CorrelationViolation-Yes.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-Fault.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-FromParts.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-MessageExchanges.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply-ToParts.bpel", ""},
				{"TestCases/betsy/basic-activities/ReceiveReply.bpel", ""},
				{"TestCases/betsy/basic-activities/Rethrow-FaultData.bpel", ""},
				{"TestCases/betsy/basic-activities/Rethrow-FaultDataUnmodified.bpel", ""},
				{"TestCases/betsy/basic-activities/Rethrow.bpel", ""},
				{"TestCases/betsy/basic-activities/Throw-CustomFault.bpel", ""},
				{"TestCases/betsy/basic-activities/Throw-CustomFaultInWsdl.bpel", ""},
				{"TestCases/betsy/basic-activities/Throw-FaultData.bpel", ""},
				{"TestCases/betsy/basic-activities/Throw-WithoutNamespace.bpel", ""},
				{"TestCases/betsy/basic-activities/Throw.bpel", ""},
				{"TestCases/betsy/basic-activities/Validate-InvalidVariables.bpel", ""},
				{"TestCases/betsy/basic-activities/Validate.bpel", ""},
				{"TestCases/betsy/basic-activities/Variables-DefaultInitialization.bpel", ""},
				{"TestCases/betsy/basic-activities/Variables-UninitializedVariableFault-Invoke.bpel", ""},
				{"TestCases/betsy/basic-activities/Variables-UninitializedVariableFault-Reply.bpel", ""},
				{"TestCases/betsy/basic-activities/Wait-For-InvalidExpressionValue.bpel", ""},
				{"TestCases/betsy/basic-activities/Wait-For.bpel", ""},
				{"TestCases/betsy/basic-activities/Wait-Until.bpel", ""},
				{"TestCases/betsy/scopes/MissingReply.bpel", ""},
				{"TestCases/betsy/scopes/MissingRequest.bpel", ""},
				{"TestCases/betsy/scopes/Scope-Compensate.bpel", ""},
				{"TestCases/betsy/scopes/Scope-CompensateScope.bpel", ""},
				{"TestCases/betsy/scopes/Scope-ComplexCompensation.bpel", ""},
				{"TestCases/betsy/scopes/Scope-CorrelationSets-InitAsync.bpel", ""},
				{"TestCases/betsy/scopes/Scope-CorrelationSets-InitSync.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-InitAsync.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-InitSync.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-OnAlarm-For.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery-For.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery-Until.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-OnAlarm-Until.bpel", ""},
				{"TestCases/betsy/scopes/Scope-EventHandlers-Parts.bpel", ""},
				{"TestCases/betsy/scopes/Scope-ExitOnStandardFault-JoinFailure.bpel", ""},
				{"TestCases/betsy/scopes/Scope-ExitOnStandardFault.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers-CatchAll.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers-CatchOrder.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers-FaultElement.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers-FaultMessageType.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers-VariableData.bpel", ""},
				{"TestCases/betsy/scopes/Scope-FaultHandlers.bpel", ""},
				{"TestCases/betsy/scopes/Scope-Isolated.bpel", ""},
				{"TestCases/betsy/scopes/Scope-MessageExchanges.bpel", ""},
				{"TestCases/betsy/scopes/Scope-PartnerLinks.bpel", ""},
				{"TestCases/betsy/scopes/Scope-RepeatableConstructCompensation.bpel", ""},
				{"TestCases/betsy/scopes/Scope-RepeatedCompensation.bpel", ""},
				{"TestCases/betsy/scopes/Scope-TerminationHandlers-FaultNotPropagating.bpel", ""},
				{"TestCases/betsy/scopes/Scope-TerminationHandlers.bpel", ""},
				{"TestCases/betsy/scopes/Scope-Variables-Overwriting.bpel", ""},
				{"TestCases/betsy/scopes/Scope-Variables.bpel", ""},
				{"TestCases/betsy/scopes/Variables-DefaultInitialization.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-BoundaryLinks.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-GraphExample.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links-JoinCondition.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links-JoinFailure.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links-ReceiveCreatingInstances.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links-SuppressJoinFailure.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links-TransitionCondition.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow-Links.bpel", ""},
				{"TestCases/betsy/structured-activities/Flow.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-CompletionCondition-NegativeBranches.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-CompletionCondition-Parallel.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-CompletionCondition-SuccessfulBranchesOnly.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-CompletionCondition.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-CompletionConditionFailure.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-NegativeStartCounter.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-NegativeStopCounter.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-Parallel.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach-TooLargeStartCounter.bpel", ""},
				{"TestCases/betsy/structured-activities/ForEach.bpel", ""},
				{"TestCases/betsy/structured-activities/If-Else.bpel", ""},
				{"TestCases/betsy/structured-activities/If-ElseIf-Else.bpel", ""},
				{"TestCases/betsy/structured-activities/If-ElseIf.bpel", ""},
				{"TestCases/betsy/structured-activities/If-InvalidExpressionValue.bpel", ""},
				{"TestCases/betsy/structured-activities/If.bpel", ""},
				{"TestCases/betsy/structured-activities/Pick-Correlations-InitAsync.bpel", ""},
				{"TestCases/betsy/structured-activities/Pick-Correlations-InitSync.bpel", ""},
				{"TestCases/betsy/structured-activities/Pick-CreateInstance.bpel", ""},
				{"TestCases/betsy/structured-activities/Pick-OnAlarm-For.bpel", ""},
				{"TestCases/betsy/structured-activities/Pick-OnAlarm-Until.bpel", ""},
				{"TestCases/betsy/structured-activities/RepeatUntil.bpel", ""},
				{"TestCases/betsy/structured-activities/RepeatUntilEquality.bpel", ""},
				{"TestCases/betsy/structured-activities/Sequence.bpel", ""},
				{"TestCases/betsy/structured-activities/While.bpel", ""},

				{"Testcases/complex/requestor/prototype-ebBP-BT-Requestor.bpel",""},
				{"Testcases/complex/responder/prototype-ebBP-BT-Responder.bpel",""},

				{"Testcases/complex/bpel_req/Transaction-rev1-btaRAandAAandTTPv1-role-1.bpel",""},
				{"Testcases/complex/bpel_resp/Transaction-rev1-btaRAandAAandTTPv1-role-2.bpel",""},

				{"Testcases/calculator/CalculatorService.bpel",""},
				{"Testcases/calculator/fluentCalculatorService.bpel",""},
		};
		return Arrays.asList(data);
	}

	@Test
	public void testValidators() throws Exception {
		ValidationResult violations = new Isabel().validate(bpel);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,violations);
		String data = "\n" + baos.toString() + "\n";

		assertEquals("BPEL: " + bpel + data, violatedRules, violations.getViolatedRules());
	}

}