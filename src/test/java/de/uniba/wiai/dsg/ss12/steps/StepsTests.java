package de.uniba.wiai.dsg.ss12.steps;

import com.google.common.io.CharStreams;
import com.sun.org.apache.xml.internal.security.Init;
import com.sun.org.apache.xml.internal.security.c14n.CanonicalizationException;
import com.sun.org.apache.xml.internal.security.c14n.Canonicalizer;
import com.sun.org.apache.xml.internal.security.c14n.InvalidCanonicalizerException;
import de.uniba.wiai.dsg.ss12.isabel.steps.DefaultValuesEnricher;
import de.uniba.wiai.dsg.ss12.isabel.steps.InheritanceRulesApplier;
import nu.xom.ParsingException;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStreamReader;

import static org.junit.Assert.assertEquals;

public class StepsTests {

	@Test
	public void testAddingDefaults() throws IOException, TransformerException,
			SAXException, ParserConfigurationException,
			InvalidCanonicalizerException, CanonicalizationException {
		String result = new DefaultValuesEnricher()
				.validateAndApplyDefaultValuesToBpelFile(getClass()
						.getResource("/steps/process.bpel").openStream());
		String expected = CharStreams.toString(new InputStreamReader(getClass()
				.getResourceAsStream("/steps/process_defaults.bpel")));
		assertXMLEquals(expected, result);
	}

	@Test
	public void testApplyingInheritanceRules() throws IOException,
			TransformerException, SAXException, ParserConfigurationException,
			ParsingException, InvalidCanonicalizerException,
			CanonicalizationException {
		String result = new InheritanceRulesApplier()
				.applyInheritanceRulesTo(getClass().getResource(
						"/steps/process_defaults.bpel").openStream());
		String expected = CharStreams.toString(new InputStreamReader(
				getClass().getResourceAsStream(
						"/steps/process_defaults_inheritance.bpel")));
		assertXMLEquals(expected, result);
	}

	@Test
	public void testApplyingInheritanceRulesWithPredefined()
			throws IOException, TransformerException, SAXException,
			ParserConfigurationException, ParsingException,
			InvalidCanonicalizerException, CanonicalizationException {
		String result = new InheritanceRulesApplier()
				.applyInheritanceRulesTo(getClass().getResource(
						"/steps/process_defaults_expressionLanguage.bpel")
						.openStream());
		String expected = CharStreams
				.toString(new InputStreamReader(
						getClass()
								.getResourceAsStream(
										"/steps/process_defaults_expressionLanguage_inheritance.bpel")));
		assertXMLEquals(expected, result);
	}

	private void assertXMLEquals(String expected, String actual)
			throws InvalidCanonicalizerException, ParserConfigurationException,
			SAXException, CanonicalizationException, IOException {
		Init.init();

		Canonicalizer canon = Canonicalizer
				.getInstance(Canonicalizer.ALGO_ID_C14N_OMIT_COMMENTS);
		String expectedCanonicalized = new String(canon.canonicalize(expected
				.getBytes()));
		String actualCanonicalized = new String(canon.canonicalize(actual
				.getBytes()));

		assertEquals(expectedCanonicalized, actualCanonicalized);
	}

}
