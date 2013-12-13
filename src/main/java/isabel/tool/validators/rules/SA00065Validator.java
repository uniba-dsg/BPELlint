package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.Standards;
import isabel.model.bpel.LinkEntity;
import isabel.model.bpel.SourceElement;
import isabel.model.bpel.TargetElement;
import isabel.tool.impl.ValidationCollector;
import nu.xom.Node;
import nu.xom.Nodes;

public class SA00065Validator extends Validator {

	public SA00065Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		checkSourceLinks();
		checkTargetLinks();
	}

	private void checkSourceLinks() {
		Nodes sourceNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:source", Standards.CONTEXT);
		for (Node source : sourceNodes) {
			checkLinkExists(new SourceElement(source));
		}
	}

	private void checkTargetLinks() {
		Nodes targetNodes = fileHandler.getBpel().getDocument()
				.query("//bpel:target", Standards.CONTEXT);
		for (Node targetNode : targetNodes) {
			checkLinkExists(new TargetElement(targetNode));
		}
	}

	private void checkLinkExists(LinkEntity linkEntity) {
		try {
			linkEntity.getLink();
		} catch (NavigationException e) {
			addViolation(linkEntity);
		}
	}

	@Override
	public int getSaNumber() {
		return 65;
	}

}
