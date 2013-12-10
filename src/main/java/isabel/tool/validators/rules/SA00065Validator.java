package isabel.tool.validators.rules;

import nu.xom.Node;
import nu.xom.Nodes;
import isabel.tool.helper.NodeHelper;
import isabel.tool.impl.Standards;
import isabel.tool.impl.ValidationCollector;
import isabel.tool.imports.ProcessContainer;

public class SA00065Validator extends Validator {

	public SA00065Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		Nodes sourceNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:source", Standards.CONTEXT);
		checkAllCorrespondingLinks(sourceNodes);
		Nodes targetNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:target", Standards.CONTEXT);
		checkAllCorrespondingLinks(targetNodes);
	}

	private void checkAllCorrespondingLinks(Nodes linkEntities) {
		for (Node linkEntity : linkEntities) {
			String linkName = new NodeHelper(linkEntity)
					.getAttribute("linkName");
			if (!correspondingLinkExists(linkEntity, linkName)) {
				addViolation(linkEntity);
			}
		}

	}

	private boolean correspondingLinkExists(Node node, String linkName) {
		NodeHelper nodeHelper = new NodeHelper(node);
		if ("process".equals(nodeHelper.getLocalName())) {
			return false;
		}
		if (!"flow".equals(nodeHelper.getLocalName())) {
			return correspondingLinkExists(node.getParent(), linkName);
		}
		Nodes link = node.query("./bpel:links/bpel:link[@name='" + linkName
				+ "']", Standards.CONTEXT);
		if (!isCorresponding(link)) {
			return correspondingLinkExists(node.getParent(), linkName);
		}
		return true;
	}

	private boolean isCorresponding(Nodes link) {
		return link.size() > 0;
	}

	@Override
	public int getSaNumber() {
		return 65;
	}

}
