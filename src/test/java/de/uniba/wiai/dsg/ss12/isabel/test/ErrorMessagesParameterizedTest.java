package de.uniba.wiai.dsg.ss12.isabel.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import de.uniba.wiai.dsg.ss12.isabel.io.ErrorMessages;

@RunWith(value = Parameterized.class)
public class ErrorMessagesParameterizedTest {

	private String saNumber;
	private String shortDescription;
	private String longDescription;

	public ErrorMessagesParameterizedTest(String saNumber,
			String shortDescription, String longDescription) {
		this.saNumber = saNumber;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] { { "SA00001", "1", "long" },
				{ "SA00002", "2", "long" }, { "SA00003", "3", "long" },
				{ "SA00005", "5, 10", "long" }, { "SA00006", "6", "long" },
				{ "SA00007", "7", "long" }, { "SA00008", "8", "long" },
				{ "SA00010", "10", "long" }, { "SA00011", "11", "long" },
				{ "SA00012", "12, 11", "long" }, { "SA00013", "13", "long" },
				{ "SA00015", "15", "long" }, { "SA00016", "16", "long" },
				{ "SA00017", "17", "long" }, { "SA00020", "20", "long" },
				{ "SA00021", "21, 10", "long" }, { "SA00022", "22", "long" },
				{ "SA00023", "23", "long" }, { "SA00024", "24", "long" },
				{ "SA00025", "25", "long" }, { "SA00044", "44", "long" },
				{ "SA00045", "45", "long" }, { "SA00046", "46", "long" },
				{ "SA00047", "47, 53", "long" }, { "SA00048", "48", "long" },
				{ "SA00050", "50", "long" }, { "SA00051", "51", "long" },
				{ "SA00052", "52", "long" }, { "SA00053", "53", "long" },
				{ "SA00054", "54", "long" }, { "SA00055", "55", "long" },
				{ "SA00059", "59", "long" }, { "SA00063", "63", "long" }, };
		return Arrays.asList(data);
	}

	@Test
	public void whenCalledGetShortWhithType1_shouldReturnInstanceOfString()
			throws Exception {
		ErrorMessages errorMessages = new ErrorMessages();

		assertEquals("", true,
				errorMessages.getShort(saNumber, 1) instanceof String);
	}

	@Test
	public void whenCalledGetLong_shouldReturnInstanceOfString()
			throws Exception {
		ErrorMessages errorMessages = new ErrorMessages();

		assertEquals("", true,
				errorMessages.getLong(saNumber) instanceof String);
	}
}
