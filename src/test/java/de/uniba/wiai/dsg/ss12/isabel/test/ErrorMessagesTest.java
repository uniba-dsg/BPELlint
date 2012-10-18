package de.uniba.wiai.dsg.ss12.isabel.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.uniba.wiai.dsg.ss12.isabel.io.ErrorMessages;

public class ErrorMessagesTest {

	private ErrorMessages failMessages;

	@Before
	public void setUp() throws Exception {
		failMessages = new ErrorMessages();
	}

	@Test
	public void givenSA00002Short_whenCalledGetShortWithSA00002AndType1_shouldReturnGiven()
			throws Exception {
		String saShort = failMessages.getShort("SA00002", 1);

		assertEquals("The short description of SA00002 is different.",
				"overloaded operation name in <portType>", saShort);
	}

	@Test
	public void givenSA00002Long_whenCalledGetLongWithSA00002_shouldReturnGiven()
			throws Exception {
		String saLong = failMessages.getLong("SA00002");

		assertEquals(
				"A WS-BPEL processor MUST reject any WSDL portType definition that includes overloaded operation names.",
				saLong);
	}

}
