package de.uniba.wiai.dsg.ss12.isabel.test;

import de.uniba.wiai.dsg.ss12.isabel.tool.Isabel;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.Violation;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(value = Parameterized.class)
public class FunctionalValidatorTests {

	private Set<Integer> violatedRules;
	private String bpel;

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
				{"TestCases/SA00001/Notification.bpel", "1"},
				{"TestCases/SA00001/SolicitResponse.bpel", "1"},
				{"TestCases/SA00002/OverloadedOperationNames.bpel", "2"},
				{"TestCases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlers.bpel", "3"},
				{"TestCases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlersInProcess.bpel", "3"},
				{"TestCases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInIndirectFaultHandlers.bpel", "3"},
				{"TestCases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInIndirectNestedFaultHandlers.bpel", "3"},
				{"TestCases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInNestedFaultHandlers.bpel", "3"},
				{"TestCases/SA00005/InvokeWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/SA00005/OnEventWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/SA00005/OnMessageWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/SA00005/ReceiveWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/SA00005/ReplyWithNonExistentPortType.bpel", "5, 10"},
				{"TestCases/SA00006/RethrowOutsideFaultHandlers.bpel", "6"},
				{"TestCases/SA00007/CompensateScopeOutsideFaultHandlers.bpel", "7"},
				{"TestCases/SA00008/CompensateOutsideFaultHandlers.bpel", "8"},
				{"TestCases/SA00010/UndefinedType-Catch-FaultElement.bpel", "10"},
				{"TestCases/SA00010/UndefinedType-Catch-FaultMessageType.bpel", "10"},
				{"TestCases/SA00010/UndefinedType-CorrelationSet.bpel", "10, 21"},
				{"TestCases/SA00010/UndefinedType-From.bpel", "10, 21"},
				{"TestCases/SA00010/UndefinedType-Invoke.bpel", "10, 5"},
				{"TestCases/SA00010/UndefinedType-OnEvent.bpel", "10, 5"},
				{"TestCases/SA00010/UndefinedType-OnMessage.bpel", "10, 5"},
				{"TestCases/SA00010/UndefinedType-PartnerLink.bpel", "10"},
				{"TestCases/SA00010/UndefinedType-Receive.bpel", "10, 5"},
				{"TestCases/SA00010/UndefinedType-Reply.bpel", "10, 5"},
				{"TestCases/SA00010/UndefinedType-To.bpel", "10, 21"},
				{"TestCases/SA00010/UndefinedType-Variable-Element.bpel", "10"},
				{"TestCases/SA00010/UndefinedType-Variable-MessageType.bpel", "10"},
				{"TestCases/SA00010/UndefinedType-Variable-Type.bpel", "10"},
				{"TestCases/SA00011/Import-WrongNameSpace.bpel", "11"},
				{"TestCases/SA00012/Import-NoNameSpace.bpel", "12, 11"},
				{"TestCases/SA00013/Import-WrongImportType.bpel", "13"},
				{"TestCases/SA00020/PropertyAlias-AllOptionalAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypeAttribute.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypeElementAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypePartElementAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypePartTypeAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypeTypeAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-MessageTypeTypeElementAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-NoOptionalAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-PartAttribute.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-PartElementAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-PartTypeAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-PartTypeElementAttributes.bpel", "20"},
				{"TestCases/SA00020/PropertyAlias-TypeElementAttributes.bpel", "20"},
				{"TestCases/SA00021/CorrelationSet-Properties-Undefined.bpel", "21, 10"},
				{"TestCases/SA00021/From-Property-Undefined.bpel", "21, 10"},
				{"TestCases/SA00021/OnEvent-Variable-Undefined.bpel", "21"},
				{"TestCases/SA00021/To-Property-Undefined.bpel", "21, 10"},
				{"TestCases/SA00022/Duplicate-propertyAliasElement.bpel", "22"},
				{"TestCases/SA00022/Duplicate-propertyAliasMessageType.bpel", "22"},
				{"TestCases/SA00022/Duplicate-propertyAliasType.bpel", "22"},
				{"TestCases/SA00023/Process-Duplicated-Variables.bpel", "23"},
				{"TestCases/SA00023/Scope-Duplicated-Variables.bpel", "23"},
				{"TestCases/SA00023/Scope-Scope-Duplicated-Variables.bpel", "23"},
				{"TestCases/SA00024/OnEvent-containing-dot.bpel", "24"},
				{"TestCases/SA00024/Variable-containing-dot.bpel", "24"},
				{"TestCases/SA00025/Variable-havingMessageTypeAndElement.bpel", "25"},
				{"TestCases/SA00025/Variable-havingTypeAndElement.bpel", "25"},
				{"TestCases/SA00025/Variable-havingTypeAndMessageType.bpel", "25"},
				{"TestCases/SA00025/Variable-havingTypeAndMessageTypeAndElement.bpel", "25"},
				{"TestCases/SA00025/Variable-missingMessageTypeAndTypeAndElement.bpel", "25"},
				{"TestCases/SA00044/Process-CorrelationSet-Ambiguous.bpel", "44"},
				{"TestCases/SA00044/Scope-CorrelationSets-Ambiguous.bpel", "44"},
				{"TestCases/SA00045/Property-TypeComplexType.bpel", "45"},
				{"TestCases/SA00045/Property-TypeMissing.bpel", "45"},
				{"TestCases/SA00046/Invoke-OneWay-Correlation-Pattern.bpel", "46"},
				{"TestCases/SA00046/Invoke-RequestResponse-Correlation-PatternMissing.bpel", "46"},
				{"TestCases/SA00047/EmptyMessage-Invoke-FromParts.bpel", "47, 53"},
				{"TestCases/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel", "47, 54, 53"},
				{"TestCases/SA00047/EmptyMessage-Invoke-ToParts.bpel", "47, 54"},
				{"TestCases/SA00047/EmptyMessage-OnEvent-FromParts.bpel", "47, 53"},
				{"TestCases/SA00047/EmptyMessage-OnMessage-FromParts.bpel", "47, 53"},
				{"TestCases/SA00047/EmptyMessage-Receive-FromParts.bpel", "47, 53"},
				{"TestCases/SA00047/EmptyMessage-Reply-ToParts.bpel", "47, 54"},
				{"TestCases/SA00047/Invoke-OneWay-NoInputVariable-NoToParts.bpel", "47"},
				{"TestCases/SA00047/Invoke-RequestResponse-NoInputOutputVariables-NoToFromParts.bpel", "47"},
				{"TestCases/SA00047/Invoke-RequestResponse-NoInputVariable-NoToParts.bpel", "47"},
				{"TestCases/SA00047/Invoke-RequestResponse-NoOutputVariable-NoFromParts.bpel", "47"},
				{"TestCases/SA00047/NoVariable-NoFromPart-OnEvent.bpel", "47"},
				{"TestCases/SA00047/NoVariable-NoFromPart-OnMessage.bpel", "47"},
				{"TestCases/SA00047/NoVariable-NoFromPart-Receive.bpel", "47"},
				{"TestCases/SA00047/NoVariable-NoToPart-NoFromPart-ReceiveReply.bpel", "47"},
				{"TestCases/SA00047/NoVariable-NoToPart-Reply.bpel", "47"},
				{"TestCases/SA00048/InputOutputVariable-Message-NotFound.bpel", "48"},
				{"TestCases/SA00048/InputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"TestCases/SA00048/InputVariable-Type-MessageManyParts.bpel", "48"},
				{"TestCases/SA00048/InputVariable-Type-MessageOnePart-NotFound.bpel", "48"},
				{"TestCases/SA00048/OutputVariable-MessageType-Message-NotFound.bpel", "48"},
				{"TestCases/SA00048/OutputVariable-Type-MessageManyParts.bpel", "48"},
				{"TestCases/SA00048/OutputVariable-Type-MessageOnePart-NotFound.bpel", "48"},
				{"TestCases/SA00050/Invoke-MissingToPart.bpel", "50"},
				{"TestCases/SA00050/Receive-MissingToPart.bpel", "50"},
				{"TestCases/SA00051/Invoke-ToPartsAndInputVariable.bpel", "51"},
				{"TestCases/SA00052/Invoke-FromPartsAndOutputVariable.bpel", "52"},
				{"TestCases/SA00053/Invoke-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/SA00053/OnEvent-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/SA00053/OnMessage-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/SA00053/Receive-FromPartDifferingFromMessageDefinition.bpel", "53"},
				{"TestCases/SA00054/Invoke-ToPartDifferingFromMessageDefinition.bpel", "54"},
				{"TestCases/SA00054/Reply-ToPartDifferingFromMessageDefinition.bpel", "54"},
				{"TestCases/SA00055/Receive-WithFromPartElementAndVariableAttribute.bpel", "55"},
				{"TestCases/SA00059/Reply-WithToPartElementAndVariableAttribute.bpel", "59"},
				{"TestCases/SA00063/OnMessage-With-FromPartAndAttributeVariable.bpel", "63"},

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
		};
		return Arrays.asList(data);
	}

	@Test
	public void testValidators() throws Exception {
		ViolationCollector violations = new Isabel().validate(bpel);

		Set<Integer> actualViolatedRules = new HashSet<>();
		for (Violation violation : violations.getResults()) {
			actualViolatedRules.add(violation.ruleNumber);
		}

		assertEquals("error in bpel file: " + bpel + " additonal data: " + violations.getResults(), violatedRules, actualViolatedRules);
	}
}
