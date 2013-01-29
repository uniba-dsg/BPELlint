package de.uniba.wiai.dsg.ss12.steps;

import com.google.common.io.CharStreams;
import de.uniba.wiai.dsg.ss12.isabel.steps.DefaultValuesEnricher;
import de.uniba.wiai.dsg.ss12.isabel.steps.InheritanceRulesApplier;
import nu.xom.ParsingException;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStreamReader;

public class StepsTests {

	@Test
	public void testAddingDefaults() throws IOException, TransformerException, SAXException, ParserConfigurationException {
		String result = new DefaultValuesEnricher().validateAndApplyDefaultValuesToBpelFile(getClass().getResource("/steps/process.bpel").openStream());
		String expected = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/steps/process_defaults.bpel")));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testApplyingInheritanceRules() throws IOException, TransformerException, SAXException, ParserConfigurationException, ParsingException {
		String result = new InheritanceRulesApplier().applyInheritanceRulesTo(getClass().getResource("/steps/process_defaults.bpel").openStream());
		String expected = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/steps/process_defaults_inheritance.bpel")));
		Assert.assertEquals(expected, result);
	}

	@Test
	public void testApplyingInheritanceRulesWithPredefined() throws IOException, TransformerException, SAXException, ParserConfigurationException, ParsingException {
		String result = new InheritanceRulesApplier().applyInheritanceRulesTo(getClass().getResource("/steps/process_defaults_expressionLanguage.bpel").openStream());
		String expected = CharStreams.toString(new InputStreamReader(getClass().getResourceAsStream("/steps/process_defaults_expressionLanguage_inheritance.bpel")));
		Assert.assertEquals(expected, result);
	}

}
