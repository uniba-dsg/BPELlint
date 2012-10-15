package de.uniba.wiai.dsg.ss12.isabel.test.validators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uniba.wiai.dsg.ss12.isabel.tool.ValidationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.XmlFileLoader;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.IsabelViolationCollector;
import de.uniba.wiai.dsg.ss12.isabel.tool.validators.Validator;

public abstract class ValidatorTests {

	protected XmlFileLoader						fileLoader;
	protected BpelProcessFiles					bpelProcessFiles;
	protected static IsabelViolationCollector	violationCollector;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		violationCollector = new IsabelViolationCollector();
	}

	public abstract Validator getValidator();

	public abstract int saNumber();

	@Before
	public void setUp() throws Exception {
		fileLoader = new XmlFileLoader();
	}

	protected void assertionEqualsCorrespondingTo(boolean isFileOfThisValidator)
			throws ValidationException {
		if (isFileOfThisValidator) {
			assertFalse(getValidator().validate());
		} else {
			assertTrue(getValidator().validate());
		}
	}

	@Test
	public void givenSaNumber_whenConstructed_getSaNumberShouldReturnSaNumber() throws Exception {
		int saNumber = getValidator().getSaNumber();
		assertEquals(saNumber(), saNumber);
	}

	@Test
	public void givenNotification_whenLoadedAllProcessFiles_validateShouldReturnSA00001Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader.loadAllProcessFiles("Testcases/SA00001/Notification.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 1);
	}

	@Test
	public void givenSolicitResponse_whenLoadedAllProcessFiles_validateShouldReturnSA00001Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader.loadAllProcessFiles("Testcases/SA00001/SolicitResponse.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 1);
	}

	@Test
	public void givenOverloadedOperationNames_whenLoadedAllProcessFiles_validateShouldReturnSA00002Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00002/OverloadedOperationNames.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 2);
	}

	@Test
	public void givenExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00003Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 3);
	}

	@Test
	public void givenExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlersInProcess_whenLoadedAllProcessFiles_validateShouldReturnSA00003Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInDirectFaultHandlersInProcess.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 3);
	}

	@Test
	public void givenExitOnStandartFaultButCatchingStandardFaultInIndirectFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00003Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInIndirectFaultHandlers.bpel");
		assertionEqualsCorrespondingTo(saNumber() == 3);
	}

	@Test
	public void givenExitOnStandartFaultButCatchingStandardFaultInIndirectNestedFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00003Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInIndirectNestedFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 3);
	}

	@Test
	public void givenExitOnStandartFaultButCatchingStandardFaultInNestedFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00003Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00003/ExitOnStandartFaultButCatchingStandardFaultInNestedFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 3);
	}

	@Test
	public void givenInvokeWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/InvokeWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Test
	public void givenOnEventWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/OnEventWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Test
	public void givenOnMessageWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/OnMessageWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Test
	public void givenReceiveWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/ReceiveWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Test
	public void givenReplyWithNonExistentPortType_whenLoadedAllProcessFiles_validateShouldReturnSA00005Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00005/ReplyWithNonExistentPortType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 5);
	}

	@Test
	public void givenRethrowOutsideFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00006Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00006/RethrowOutsideFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 6);
	}

	@Test
	public void givenCompensateScopeOutsideFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00007Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00007/CompensateScopeOutsideFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 7);
	}

	@Test
	public void givenCompensateOutsideFaultHandlers_whenLoadedAllProcessFiles_validateShouldReturnSA00008Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00008/CompensateOutsideFaultHandlers.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 8);
	}

	@Test
	public void givenUndefinedTypeCatchFaultElement_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Catch-FaultElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeCatchFaultMessageType_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Catch-FaultMessageType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeCorrelationSet_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-CorrelationSet.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeFrom_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-From.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeInvoke_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Invoke.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeOnEvent_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-OnEvent.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeOnMessage_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-OnMessage.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypePartnerLink_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-PartnerLink.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeReceive_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Receive.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeReply_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Reply.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeTo_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-To.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeVariableElement_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Variable-Element.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeVariableMessageType_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Variable-MessageType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenUndefinedTypeVariableType_whenLoadedAllProcessFiles_validateShouldReturnSA00010Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00010/UndefinedType-Variable-Type.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 10);
	}

	@Test
	public void givenImportWrongNameSpace_whenLoadedAllProcessFiles_validateShouldReturnSA00011Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00011/Import-WrongNameSpace.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 11);
	}

	@Test
	public void givenImportNoNameSpace_whenLoadedAllProcessFiles_validateShouldReturnSA00012Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00012/Import-NoNameSpace.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 12);
	}

	@Test
	public void givenImportWrongImportType_whenLoadedAllProcessFiles_validateShouldReturnSA00013Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00013/Import-WrongImportType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 13);
	}

	@Test
	public void givenPropertyAliasAllOptionalAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-AllOptionalAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypeAttribute_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypeAttribute.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypeElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypeElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypePartElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypePartElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypePartTypeAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypePartTypeAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypeTypeAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypeTypeAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasMessageTypeTypeElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-MessageTypeTypeElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasNoOptionalAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-NoOptionalAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasPartAttribute_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-PartAttribute.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasPartElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-PartElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasPartTypeAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-PartTypeAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasPartTypeElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-PartTypeElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenPropertyAliasTypeElementAttributes_whenLoadedAllProcessFiles_validateShouldReturnSA00020Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00020/PropertyAlias-TypeElementAttributes.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 20);
	}

	@Test
	public void givenCorrelationSetPropertiesUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/CorrelationSet-Properties-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Test
	public void givenFromPropertyUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/From-Property-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Test
	public void givenOnEventVariableUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/OnEvent-Variable-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Test
	public void givenToPropertyUndefined_whenLoadedAllProcessFiles_validateShouldReturnSA00021Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00021/To-Property-Undefined.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 21);
	}

	@Test
	public void givenDuplicatePropertyAliasElement_whenLoadedAllProcessFiles_validateShouldReturnSA00022Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00022/duplicate-propertyAliasElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 22);
	}

	@Test
	public void givenDuplicatePropertyAliasMessageType_whenLoadedAllProcessFiles_validateShouldReturnSA00022Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00022/duplicate-propertyAliasMessageType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 22);
	}

	@Test
	public void givenDuplicatePropertyAliasType_whenLoadedAllProcessFiles_validateShouldReturnSA00022Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00022/duplicate-propertyAliasType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 22);
	}

	@Test
	public void givenProcessDuplicatedVariables_whenLoadedAllProcessFiles_validateShouldReturnSA00023Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00023/Process-Duplicated-Variables.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 23);
	}

	@Test
	public void givenScopeDuplicatedVariables_whenLoadedAllProcessFiles_validateShouldReturnSA00023Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00023/Scope-Duplicated-Variables.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 23);
	}

	@Test
	public void givenScopeScopeDuplicatedVariables_whenLoadedAllProcessFiles_validateShouldReturnSA00023Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00023/Scope-Scope-Duplicated-Variables.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 23);
	}

	@Test
	public void givenVariableNamecontainingdot_whenLoadedAllProcessFiles_validateShouldReturnSA00024Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00024/Variable-containing-dot.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 24);
	}

	@Test
	public void givenOnEventVariablecontainingdot_whenLoadedAllProcessFiles_validateShouldReturnSA00024Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00024/OnEvent-containing-dot.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 24);
	}

	@Test
	public void givenVariableMissingTypeAndMessageTypeAndElement_whenLoadedAllProcessFiles_validateShouldReturnSA00025Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00025/Variable-missingMessageTypeAndTypeAndElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 25);
	}

	@Test
	public void givenVariableHavingTypeAndMessageType_whenLoadedAllProcessFiles_validateShouldReturnSA00025Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00025/Variable-havingTypeAndMessageType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 25);
	}

	@Test
	public void givenVariableHavingTypeAndElement_whenLoadedAllProcessFiles_validateShouldReturnSA00025Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00025/Variable-havingTypeAndElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 25);
	}

	@Test
	public void givenVariableHavingMessageTypeAndElement_whenLoadedAllProcessFiles_validateShouldReturnSA00025Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00025/Variable-havingMessageTypeAndElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 25);
	}

	@Test
	public void givenVariableHavingTypeAndMessageTypeAndElement_whenLoadedAllProcessFiles_validateShouldReturnSA00025Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00025/Variable-havingTypeAndMessageTypeAndElement.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 25);
	}

	@Test
	public void givenProcessCorrelationSetAmbiguous_whenLoadedAllProcessFiles_validateShouldReturnSA00044Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00044/Process-CorrelationSet-Ambiguous.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 44);
	}

	@Test
	public void givenScopeCorrelationSetsAmbiguous_whenLoadedAllProcessFiles_validateShouldReturnSA00044Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00044/Scope-CorrelationSets-Ambiguous.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 44);
	}

	@Test
	public void givenPropertyTypeComplexType_whenLoadedAllProcessFiles_validateShouldReturnSA00045Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00045/Property-TypeComplexType.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 45);
	}

	@Test
	public void givenPropertyTypeMissing_whenLoadedAllProcessFiles_validateShouldReturnSA00045Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00045/Property-TypeMissing.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 45);
	}

	@Test
	public void givenInvokeOneWayCorrelationPattern_whenLoadedAllProcessFiles_validateShouldReturnSA00046Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00046/Invoke-OneWay-Correlation-Pattern.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 46);
	}

	@Test
	public void givenInvokeRequestResponseCorrelationPatternMissing_whenLoadedAllProcessFiles_validateShouldReturnSA00046Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00046/Invoke-RequestResponse-Correlation-PatternMissing.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 46);
	}

	@Test
	public void givenEmptyMessageInvokeFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageInvokeToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-ToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageInvokeToPartsFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Invoke-ToParts-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageOnEventFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-OnEvent-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageOnMessageFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-OnMessage-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageReceiveFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Receive-FromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenEmptyMessageReplyToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/EmptyMessage-Reply-ToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenInvokeOneWayNoInputVariableAndToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/Invoke-OneWay-NoInputVariable-NoToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenInvokeRequestResponseNoInputVariableAndToParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/Invoke-RequestResponse-NoInputVariable-NoToParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenInvokeRequestResponseNoOutputVariableAndFromParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/Invoke-RequestResponse-NoOutputVariable-NoFromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenInvokeRequestResponseNoVariablesAndParts_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/Invoke-RequestResponse-NoInputOutputVariables-NoToFromParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenOnEventNoVariableAndFromPart_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/NoVariable-NoFromPart-OnEvent.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenOnMessageNoVariableAndFromPart_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/NoVariable-NoFromPart-OnMessage.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenReceiveNoVariableAndFromPart_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/NoVariable-NoFromPart-Receive.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenReceiveReplyNoVariableAndToPartAndFromPart_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/NoVariable-NoToPart-NoFromPart-ReceiveReply.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenReplyNoVariableAndToPart_whenLoadedAllProcessFiles_validateShouldReturnSA00047Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00047/NoVariable-NoToPart-Reply.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 47);
	}

	@Test
	public void givenInputVariableMessageTypeMessageNotFound_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/InputVariable-MessageType-Message-NotFound.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenInputVariableTypeMessageManyParts_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/InputVariable-Type-MessageManyParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenInputVariableTypeMessageOnePartNotFound_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/InputVariable-Type-MessageOnePart-NotFound.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenOutputVariableMessageTypeMessageNotFound_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/OutputVariable-MessageType-Message-NotFound.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenOutputVariableTypeMessageOnePartNotFound_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/OutputVariable-Type-MessageOnePart-NotFound.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenOutputVariableTypeMessageManyParts_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/OutputVariable-Type-MessageManyParts.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenInputOutputVariableMessageNotFound_whenLoadedAllProcessFiles_validateShouldReturnSA00048Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00048/InputOutputVariable-Message-NotFound.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 48);
	}

	@Test
	public void givenInvokeMissingToPart_whenLoadedAllProcessFiles_validateShouldReturnSA00050Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00050/Invoke-MissingToPart.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 50);
	}

	@Test
	public void givenReceiveMissingToPart_whenLoadedAllProcessFiles_validateShouldReturnSA00050Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00050/Receive-MissingToPart.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 50);
	}

	@Test
	public void givenInvokeToPartsAndInputVariable_whenLoadedAllProcessFiles_validateShouldReturnSA00051Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00051/Invoke-ToPartsAndInputVariable.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 51);
	}

	@Test
	public void givenInvokeFromPartsAndOutputVariable_whenLoadedAllProcessFiles_validateShouldReturnSA00052Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00052/Invoke-FromPartsAndOutputVariable.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 52);
	}

	@Test
	public void givenReceiveFromPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00053Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00053/Receive-FromPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Test
	public void givenInvokeFromPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00053Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00053/Invoke-FromPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Test
	public void givenOnEventFromPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00053Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00053/OnEvent-FromPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Test
	public void givenOnMessageFromPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00053Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00053/OnMessage-FromPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 53);
	}

	@Test
	public void givenReplyToPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00054Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00054/Reply-ToPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 54);
	}

	@Test
	public void givenInvokeToPartDifferingFromMessageDefinition_whenLoadedAllProcessFiles_validateShouldReturnSA00054Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00054/Invoke-ToPartDifferingFromMessageDefinition.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 54);
	}

	@Test
	public void givenReceiveWithFromPartElementAndVariableAttribute_whenLoadedAllProcessFiles_validateShouldReturnSA00055Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00055/Receive-WithFromPartElementAndVariableAttribute.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 55);
	}

	@Test
	public void givenReplyWithToPartElementAndVariableAttribute_whenLoadedAllProcessFiles_validateShouldReturnSA00059Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00059/Reply-WithToPartElementAndVariableAttribute.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 59);
	}

	@Test
	public void givenOnMessageWithFromPartAndAttributeVariable_whenLoadedAllProcessFiles_validateShouldReturnSA00063Compliant()
			throws Exception {
		bpelProcessFiles = fileLoader
				.loadAllProcessFiles("Testcases/SA00063/OnMessage-With-FromPartAndAttributeVariable.bpel");

		assertionEqualsCorrespondingTo(saNumber() == 63);
	}

}
