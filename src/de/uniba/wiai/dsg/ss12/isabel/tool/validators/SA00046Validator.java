package de.uniba.wiai.dsg.ss12.isabel.tool.validators;

import static de.uniba.wiai.dsg.ss12.isabel.tool.Standards.CONTEXT;
import nu.xom.Node;
import nu.xom.Nodes;
import de.uniba.wiai.dsg.ss12.isabel.tool.NavigationException;
import de.uniba.wiai.dsg.ss12.isabel.tool.Standards;
import de.uniba.wiai.dsg.ss12.isabel.tool.imports.BpelProcessFiles;
import de.uniba.wiai.dsg.ss12.isabel.tool.reports.ViolationCollector;

public class SA00046Validator extends Validator {
	public SA00046Validator(BpelProcessFiles files,
			ViolationCollector violationCollector) {
		super(files, violationCollector);
	}

	@Override
	public void validate() {
		Nodes invokeCorrelationsNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:invoke/bpel:correlations", CONTEXT);

		for (Node node : invokeCorrelationsNodes) {
			try {
				Node operation = navigator.correspondingOperation(node
						.getParent());

				if (new OperationHelper(operation).isRequestResponse()) {
					reportViolation(getCorrelationWithoutPattern(node), 1);
				} else if (new OperationHelper(operation).isOneWay()) {
					reportViolation(getCorrelationWithPattern(node), 2);
				}
			} catch (NavigationException e) {
				// This node could not be validated
			}

		}
	}

	private void reportViolation(Nodes correlations, int type) {
		for (Node node : correlations) {
			addViolation(getBpelFileName(), node, type);
		}
	}

	private Nodes getCorrelationWithPattern(Node correlationsElement) {
		return correlationsElement.query(
				"child::bpel:correlation[attribute::pattern]",
				Standards.CONTEXT);
	}

	private Nodes getCorrelationWithoutPattern(Node correlationsElement) {
		return correlationsElement.query(
				"child::bpel:correlation[not(attribute::pattern)]",
				Standards.CONTEXT);
	}

	@Override
	public int getSaNumber() {
		return 46;
	}

}
