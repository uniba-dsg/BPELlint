package bpellint.tool.validators.rules;

import java.util.LinkedList;
import java.util.List;

import bpellint.model.NodeHelper;
import bpellint.model.NodesUtil;
import bpellint.model.ProcessContainer;
import bpellint.model.Standards;
import bpellint.tool.validators.result.ValidationCollector;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00061Validator extends Validator {

	private static final int HAS_NO_MESSAGE_EXCHANGE_REPLY = 1; 
	private static final int HAS_NO_MESSAGE_EXCHANGE_IMA = 2; 
	private static final int MESSAGE_EXCHANGE_IS_MISSING = 3; 
	
	public SA00061Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<Node> messageActivities = getMessageActivities();

		for (Node node : messageActivities) {
			if (!hasMessageExchangePartner(node, "reply")) {
				addViolation(node,HAS_NO_MESSAGE_EXCHANGE_REPLY);
			}
		}

		List<Node> replyNodes = getReplies();
		for (Node node : replyNodes) {
			if (!hasMessageExchangePartner(node, "receive")
					&& !hasMessageExchangePartner(node, "onMessage")
					&& !hasMessageExchangePartner(node, "onEvent")) {
				addViolation(node, HAS_NO_MESSAGE_EXCHANGE_IMA);
			}
		}

		messageActivities.addAll(replyNodes);
		for (Node node : messageActivities) {
			if (!hasMessageExchange(node)) {
				addViolation(node, MESSAGE_EXCHANGE_IS_MISSING);
			}
		}
	}

	private List<Node> getMessageActivities() {
		Nodes receiveNodes = processContainer.getBpel().getDocument()
				.query("//bpel:receive[@messageExchange]", Standards.CONTEXT);
		Nodes onEventNodes = processContainer.getBpel().getDocument()
				.query("//bpel:onEvent[@messageExchange]", Standards.CONTEXT);
		Nodes onMessageNodes = processContainer.getBpel().getDocument()
				.query("//bpel:onMessage[@messageExchange]", Standards.CONTEXT);

		List<Node> messageActivities = new LinkedList<>();
		messageActivities.addAll(NodesUtil.toList(receiveNodes));
		messageActivities.addAll(NodesUtil.toList(onEventNodes));
		messageActivities.addAll(NodesUtil.toList(onMessageNodes));

		return messageActivities;
	}

	private boolean hasMessageExchangePartner(Node node, String partner) {
		String messageExchange = new NodeHelper(node)
				.getAttribute("messageExchange");
		return hasCorrespondingPartnerInScope(node, partner, messageExchange);
	}

	private boolean hasCorrespondingPartnerInScope(Node node, String partner,
			String messageExchange) {
		NodeHelper nodeHelper = new NodeHelper(node);
		if (!"scope".equals(nodeHelper.getLocalName())
				&& !"process".equals(nodeHelper.getLocalName())) {
			return hasCorrespondingPartnerInScope(node.getParent(), partner,
					messageExchange);
		}
		Nodes partnerNode = node.query("//bpel:" + partner
				+ "[@messageExchange='" + messageExchange + "']",
				Standards.CONTEXT);
		if (!(partnerNode.size() == 1)) {
			if ("process".equals(nodeHelper.getLocalName())) {
				return false;
			} else {
				return hasCorrespondingPartnerInScope(node.getParent(),
						partner, messageExchange);
			}
		}

		return true;
	}

	private List<Node> getReplies() {
        return NodesUtil.toList(processContainer.getBpel()
                .getDocument()
                .query("//bpel:reply[@messageExchange]", Standards.CONTEXT));
	}

	private boolean hasMessageExchange(Node node) {
		String messageExchange = new NodeHelper(node)
				.getAttribute("messageExchange");
		return isMessageExchangeInScope(node, messageExchange);
	}
	
	private boolean isMessageExchangeInScope(Node node, String messageExchange) {
		NodeHelper nodeHelper = new NodeHelper(node);
		if ("onEvent".equals(nodeHelper.getLocalName())) {
			// SA00089 allows onEvents to use messageExchanges defined in the associated scope
			if (node.query("./bpel:scope/bpel:messageExchanges/bpel:messageExchange[@name='" + messageExchange + "']", Standards.CONTEXT).hasAny()) {
				return true;
			} 
		}
		if (!"scope".equals(nodeHelper.getLocalName())
				&& !"process".equals(nodeHelper.getLocalName())) {
			return isMessageExchangeInScope(node.getParent(), 
					messageExchange);
		}
		Nodes messageExchangeDefinition = node.query("./bpel:messageExchanges/bpel:messageExchange[@name='" + messageExchange + "']",
				Standards.CONTEXT);
		if (!(messageExchangeDefinition.size() == 1)) {
			if ("process".equals(nodeHelper.getLocalName())) {
				return false;
			} else {
				return isMessageExchangeInScope(node.getParent(),
						messageExchange);
			}
		}

		return true;
	}

	@Override
	public int getSaNumber() {
		return 61;
	}

}
