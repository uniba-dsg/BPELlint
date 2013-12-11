package isabel.tool.validators.rules;

import isabel.model.NodeHelper;
import isabel.model.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.model.ProcessContainer;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00067Validator extends Validator {

	public SA00067Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes sourcesNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:sources", Standards.CONTEXT);
		for (Node sources : sourcesNodes) {
			checkForDoubleLinks(sources);
		}
	}

	private void checkForDoubleLinks(Node sources) {
		Set<String> sourceLinkNames = collectLinkNames(sources);

		Nodes targetsNodesInSoroundingFlow = sources.query(
				"./../../..//bpel:targets", Standards.CONTEXT);
		for (Node targets : targetsNodesInSoroundingFlow) {
			Set<String> targetsLinkNames = collectLinkNames(targets);
			if (!haveMaxOneSharedLink(sourceLinkNames, targetsLinkNames)) {
				addViolation(sources);
				addViolation(targets);
			}
		}
	}

	private Set<String> collectLinkNames(Node linkEntityContainer) {
		String linkEntityType = determineType(linkEntityContainer);
		Nodes linkEntityNodes = linkEntityContainer.query("./bpel:"
				+ linkEntityType, Standards.CONTEXT);

		Set<String> linkNames = new HashSet<>();
		for (Node linkEntityNode : linkEntityNodes) {
			linkNames.add(new NodeHelper(linkEntityNode)
					.getAttribute("linkName"));
		}

		return linkNames;
	}

	private String determineType(Node linkEntityContainer) {
		String linkEntityType = new NodeHelper(linkEntityContainer)
				.getLocalName();
		String linkEntityTypeWithoutEndingLetter_S = linkEntityType.substring(
				0, linkEntityType.length() - 1);
		return linkEntityTypeWithoutEndingLetter_S;
	}

	private boolean haveMaxOneSharedLink(Set<String> sourceLinkNames,
			Set<String> targetsLinkNames) {
		return Sets.intersection(sourceLinkNames, targetsLinkNames).size() < 2;
	}

	@Override
	public int getSaNumber() {
		return 67;
	}

}
