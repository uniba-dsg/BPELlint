package de.uniba.wiai.dsg.ss12.isabel.steps;

import com.google.common.base.Joiner;
import nu.xom.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

public class InheritanceRulesApplier {

	public String applyInheritanceRulesTo(InputStream bpelFile) throws ParsingException, IOException {
		Document doc = new Builder().build(bpelFile);

		applyExpressionLanguage(doc);
		applyQueryLanguage(doc);

		applyExitOnStandardFailure(doc);
		applySuppressJoinFailure(doc);

		return doc.toXML();
	}

	private void applyQueryLanguage(Document doc) {
		String attributeName = "queryLanguage";
		String[] elements = {"query"};

		applyAttributeToElements(doc, attributeName, elements);
	}

	private void applyExpressionLanguage(Document doc) {
		String attributeName = "expressionLanguage";
		String[] elements = {"branches", "condition", "finalCounterValue", "for", "from", "joinCondition", "repeatEvery", "startCounterValue", "to", "transitionCondition"};

		applyAttributeToElements(doc, attributeName, elements);
	}

	private void applyAttributeToElements(Document doc, String attributeName, String[] elements) {
		Attribute expressionLanguage = doc.getRootElement().getAttribute(attributeName);

		//create query
		String[] queryElements = new String[elements.length];
		for (int i = 0; i < elements.length; i++) {
			queryElements[i] = "//bpel:" + elements[i];
		}
		String query = Joiner.on("|").join(queryElements);

		//add attribute if not present
		Nodes nodes = doc.getRootElement().query(query, new XPathContext("bpel", "http://docs.oasis-open.org/wsbpel/2.0/process/executable"));
		for (Node node : nodes) {
			Element element = (Element) node;
			if (element.getAttribute(attributeName) == null) {
				element.addAttribute((Attribute) expressionLanguage.copy());
			}
		}
	}

	private void applyExitOnStandardFailure(Document doc) {
		String attributeName = "exitOnStandardFault";
		String[] elementNames = {"scope"};

		Element root = doc.getRootElement();
		Attribute attribute = root.getAttribute(attributeName);

		applyAttributeRecursively(root, attribute, attributeName, Arrays.asList(elementNames));
	}

	private void applySuppressJoinFailure(Document doc) {
		String attributeName = "suppressJoinFailure";
		String[] elementNames = {"assign", "compensate", "compensateScope", "empty", "exit", "flow","forEach", "if", "invoke", "pick", "receive", "repeatUntil", "reply", "rethrow", "scope", "sequence", "throw","validate", "wait","while"};

		Element root = doc.getRootElement();
		Attribute attribute = root.getAttribute(attributeName);

		applyAttributeRecursively(root, attribute, attributeName, Arrays.asList(elementNames));
	}

	private void applyAttributeRecursively(Element root, Attribute exitOnStandardFaultAttribute, String attributeName, List<String> elementNames) {
		Elements children = root.getChildElements();
		for (int i = 0; i < children.size(); i++) {
			Element child = children.get(i);
			if (child.getNamespaceURI().equals("http://docs.oasis-open.org/wsbpel/2.0/process/executable") && elementNames.contains(child.getLocalName())) {
				if (child.getAttribute(attributeName) == null) {
					child.addAttribute((Attribute) exitOnStandardFaultAttribute.copy());
					// recursion with previous attribute
					applyAttributeRecursively(child, exitOnStandardFaultAttribute, attributeName, elementNames);
				} else {
					// recursion with new attribute
					applyAttributeRecursively(child, child.getAttribute(attributeName), attributeName, elementNames);
				}

			} else {
				//recursion with previous attribute
				applyAttributeRecursively(child, exitOnStandardFaultAttribute, attributeName, elementNames);
			}
		}
	}

}
