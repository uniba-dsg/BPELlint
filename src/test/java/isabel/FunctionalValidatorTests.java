package isabel;

import isabel.io.ValidationResultPrinter;
import isabel.io.VerbosityLevel;
import isabel.tool.Isabel;
import isabel.tool.ValidationResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
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

	@Parameterized.Parameters(name = "{index}: {0} violates {1}")
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][]{
// SA violation tests
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

				{"Testcases/rules/SA00059/Reply-WithToPartElementAndVariableAttribute.bpel", "59"},

				{"Testcases/rules/SA00062/Pick-CreateInstanceWithOnAlarm.bpel", "62"},

				{"Testcases/rules/SA00063/OnMessage-With-FromPartAndAttributeVariable.bpel", "63"},

				{"Testcases/rules/SA00076/ForEach-DuplicateCounterVariable.bpel", "76"},

				{"Testcases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},
				{"Testcases/rules/SA00080/EmptyFaultHandlersInProcess.bpel", "80"},

				{"Testcases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},
				{"Testcases/rules/SA00083/EmptyEventHandlersInProcess.bpel", "83"},

				// betsy tests
				{"Testcases/betsy/basic-activities/Assign-Copy-QueryLanguage.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-ExpressionLanguage-From.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-ExpressionLanguage-To.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-PartnerLink-PartnerRole.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-To-Query.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-To-QueryLanguage.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-To-Property.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-DoXslTransform-InvalidSourceFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-DoXslTransform-SubLanguageExecutionFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-DoXslTransform-XsltStylesheetNotFound.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-DoXslTransform.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-GetVariableProperty.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-IgnoreMissingFromData.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-KeepSrcElementName.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Copy-Query.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Empty.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Expression-From.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Expression-To.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Literal.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-PartnerLink-UnsupportedReference.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-PartnerLink.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Property.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-SelectionFailure.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-Validate.bpel", ""},
				{"Testcases/betsy/basic-activities/Assign-VariablesUnchangedInspiteOfFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Empty.bpel", ""},
				{"Testcases/betsy/basic-activities/Exit.bpel", ""},
				{"Testcases/betsy/basic-activities/ExtensionActivity-MustUnderstand.bpel", ""},
				{"Testcases/betsy/basic-activities/ExtensionActivity-NoMustUnderstand.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Async.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Catch.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-CatchAll.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-CompensationHandler.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Correlation-Pattern-InitAsync.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Correlation-Pattern-InitSync.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Empty.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-FromParts.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Sync-Fault.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-Sync.bpel", ""},
				{"Testcases/betsy/basic-activities/Invoke-ToParts.bpel", ""},
				{"Testcases/betsy/basic-activities/Receive-AmbiguousReceiveFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Receive-ConflictingReceiveFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Receive-Correlation-InitAsync.bpel", ""},
				{"Testcases/betsy/basic-activities/Receive-Correlation-InitSync.bpel", ""},
				{"Testcases/betsy/basic-activities/Receive.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-ConflictingRequestFault.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-Correlation-InitAsync.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-Correlation-InitSync.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-CorrelationViolation-Join.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-CorrelationViolation-No.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-CorrelationViolation-Yes.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-Fault.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-FromParts.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-MessageExchanges.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply-ToParts.bpel", ""},
				{"Testcases/betsy/basic-activities/ReceiveReply.bpel", ""},
				{"Testcases/betsy/basic-activities/Rethrow-FaultData.bpel", ""},
				{"Testcases/betsy/basic-activities/Rethrow-FaultDataUnmodified.bpel", ""},
				{"Testcases/betsy/basic-activities/Rethrow.bpel", ""},
				{"Testcases/betsy/basic-activities/Throw-CustomFault.bpel", ""},
				{"Testcases/betsy/basic-activities/Throw-CustomFaultInWsdl.bpel", ""},
				{"Testcases/betsy/basic-activities/Throw-FaultData.bpel", ""},
				{"Testcases/betsy/basic-activities/Throw-WithoutNamespace.bpel", ""},
				{"Testcases/betsy/basic-activities/Throw.bpel", ""},
				{"Testcases/betsy/basic-activities/Validate-InvalidVariables.bpel", ""},
				{"Testcases/betsy/basic-activities/Validate.bpel", ""},
				{"Testcases/betsy/basic-activities/Variables-DefaultInitialization.bpel", ""},
				{"Testcases/betsy/basic-activities/Variables-UninitializedVariableFault-Invoke.bpel", ""},
				{"Testcases/betsy/basic-activities/Variables-UninitializedVariableFault-Reply.bpel", ""},
				{"Testcases/betsy/basic-activities/Wait-For-InvalidExpressionValue.bpel", ""},
				{"Testcases/betsy/basic-activities/Wait-For.bpel", ""},
				{"Testcases/betsy/basic-activities/Wait-Until.bpel", ""},
				{"Testcases/betsy/scopes/MissingReply.bpel", ""},
				{"Testcases/betsy/scopes/MissingRequest.bpel", ""},
				{"Testcases/betsy/scopes/Scope-Compensate.bpel", ""},
				{"Testcases/betsy/scopes/Scope-CompensateScope.bpel", ""},
				{"Testcases/betsy/scopes/Scope-ComplexCompensation.bpel", ""},
				{"Testcases/betsy/scopes/Scope-CorrelationSets-InitAsync.bpel", ""},
				{"Testcases/betsy/scopes/Scope-CorrelationSets-InitSync.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-InitAsync.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-InitSync.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-OnAlarm-For.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery-For.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery-Until.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-OnAlarm-RepeatEvery.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-OnAlarm-Until.bpel", ""},
				{"Testcases/betsy/scopes/Scope-EventHandlers-Parts.bpel", ""},
				{"Testcases/betsy/scopes/Scope-ExitOnStandardFault-JoinFailure.bpel", ""},
				{"Testcases/betsy/scopes/Scope-ExitOnStandardFault.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers-CatchAll.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers-CatchOrder.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers-FaultElement.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers-FaultMessageType.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers-VariableData.bpel", ""},
				{"Testcases/betsy/scopes/Scope-FaultHandlers.bpel", ""},
				{"Testcases/betsy/scopes/Scope-Isolated.bpel", ""},
				{"Testcases/betsy/scopes/Scope-MessageExchanges.bpel", ""},
				{"Testcases/betsy/scopes/Scope-PartnerLinks.bpel", ""},
				{"Testcases/betsy/scopes/Scope-RepeatableConstructCompensation.bpel", ""},
				{"Testcases/betsy/scopes/Scope-RepeatedCompensation.bpel", ""},
				{"Testcases/betsy/scopes/Scope-TerminationHandlers-FaultNotPropagating.bpel", ""},
				{"Testcases/betsy/scopes/Scope-TerminationHandlers.bpel", ""},
				{"Testcases/betsy/scopes/Scope-Variables-Overwriting.bpel", ""},
				{"Testcases/betsy/scopes/Scope-Variables.bpel", ""},
				{"Testcases/betsy/scopes/Variables-DefaultInitialization.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-BoundaryLinks.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-GraphExample.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links-JoinCondition.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links-JoinFailure.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links-ReceiveCreatingInstances.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links-SuppressJoinFailure.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links-TransitionCondition.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow-Links.bpel", ""},
				{"Testcases/betsy/structured-activities/Flow.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-CompletionCondition-NegativeBranches.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-CompletionCondition-Parallel.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-CompletionCondition-SuccessfulBranchesOnly.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-CompletionCondition.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-CompletionConditionFailure.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-NegativeStartCounter.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-NegativeStopCounter.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-Parallel.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach-TooLargeStartCounter.bpel", ""},
				{"Testcases/betsy/structured-activities/ForEach.bpel", ""},
				{"Testcases/betsy/structured-activities/If-Else.bpel", ""},
				{"Testcases/betsy/structured-activities/If-ElseIf-Else.bpel", ""},
				{"Testcases/betsy/structured-activities/If-ElseIf.bpel", ""},
				{"Testcases/betsy/structured-activities/If-InvalidExpressionValue.bpel", ""},
				{"Testcases/betsy/structured-activities/If.bpel", ""},
				{"Testcases/betsy/structured-activities/Pick-Correlations-InitAsync.bpel", ""},
				{"Testcases/betsy/structured-activities/Pick-Correlations-InitSync.bpel", ""},
				{"Testcases/betsy/structured-activities/Pick-CreateInstance.bpel", ""},
				{"Testcases/betsy/structured-activities/Pick-OnAlarm-For.bpel", ""},
				{"Testcases/betsy/structured-activities/Pick-OnAlarm-Until.bpel", ""},
				{"Testcases/betsy/structured-activities/RepeatUntil.bpel", ""},
				{"Testcases/betsy/structured-activities/RepeatUntilEquality.bpel", ""},
				{"Testcases/betsy/structured-activities/Sequence.bpel", ""},
				{"Testcases/betsy/structured-activities/While.bpel", ""},

				{"Testcases/complex/requestor/prototype-ebBP-BT-Requestor.bpel", ""},
				{"Testcases/complex/responder/prototype-ebBP-BT-Responder.bpel", ""},

				{"Testcases/complex/bpel_req/Transaction-rev1-btaRAandAAandTTPv1-role-1.bpel", ""},
				{"Testcases/complex/bpel_resp/Transaction-rev1-btaRAandAAandTTPv1-role-2.bpel", ""},

				{"Testcases/calculator/CalculatorService.bpel", ""},
				{"Testcases/calculator/fluentCalculatorService.bpel", ""},

		};
		return Arrays.asList(data);
	}

	@Test
	public void testValidators() throws Exception {
		ValidationResult validationResult = new Isabel().validate(bpel);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		new ValidationResultPrinter(ps).printResults(VerbosityLevel.NORMAL,
				validationResult);
		String data = "\n" + baos.toString() + "\n";

		assertEquals("BPEL: " + bpel + data, violatedRules,
				validationResult.getViolatedRules());
	}

}
