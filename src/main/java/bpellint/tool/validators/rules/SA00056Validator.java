package bpellint.tool.validators.rules;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import bpellint.model.ComparableNode;
import bpellint.model.NavigationException;
import bpellint.model.NodeHelper;
import bpellint.model.ProcessContainer;
import bpellint.model.Referable;
import bpellint.model.Standards;
import bpellint.model.bpel.PickElement;
import bpellint.model.bpel.ScopeElement;
import bpellint.model.bpel.flow.TargetElement;
import bpellint.model.bpel.mex.ReceiveElement;
import bpellint.model.bpel.mex.StartActivity;
import bpellint.tool.validators.result.ValidationCollector;

import nu.xom.Node;
import nu.xom.Nodes;

public class SA00056Validator extends Validator {

	private static final int PRECEDING_ACTIVITY_CREATES_NO_INSTANCE = 1;
	private static final int FLOW_HAS_STARTING_AND_NON_STARTING_BRANCHES = 2;
	private static final int MUST_NOT_CONTAIN_START_ACTIVITY = 3;

	private int amountOfHiddenStartActivities;
	private List<ComparableNode> startBranches;
	private List<ComparableNode> nonStartBranches;

	public SA00056Validator(ProcessContainer files, ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		amountOfHiddenStartActivities = processContainer.getAllStartActivities().size();
		ScopeElement process = processContainer.getProcess();
		findStartActivities(childActivity(process), false);
		if (amountOfHiddenStartActivities > 0) {
			findStartActivitiesInHandler();
		}
	}

	private LinkedList<ComparableNode> listChildrenActivities(Referable referable) {
		Nodes nodes = referable.toXOM().query("./*");
		LinkedList<ComparableNode> elements = new LinkedList<>();
		for (Node node : nodes) {
			elements.add(new ComparableNode(node));
		}
		Collections.sort(elements);
		return elements;
	}

	private void findStartActivities(NodeHelper activity, boolean isInFlow) {
		if (amountOfHiddenStartActivities == 0 && !isInFlow) {
			return;
		}

		switch (activity.getLocalName()) {

		case "scope":
			findStartActivities(childActivity(activity), false);
			break;

		case "sequence":
			int endSize = amountOfHiddenStartActivities
					- activity.toXOM().query(".//*[@createInstance='yes']").size();
			for (ComparableNode childActivity : listChildrenActivities(activity)) {
				findStartActivities(new NodeHelper(childActivity), false);
				if (endSize == amountOfHiddenStartActivities) {
					return;
				}
			}
			break;

		case "flow":
			analyzeFlow(activity);
			break;

		case "receive":
			ReceiveElement receive = new ReceiveElement(activity.toXOM(), processContainer);
			if (receive.isStartActivity()) {
				amountOfHiddenStartActivities--;
			} else {
				addViolation(receive, PRECEDING_ACTIVITY_CREATES_NO_INSTANCE);
			}
			break;

		case "pick":
			PickElement pick = new PickElement(activity.toXOM(), processContainer);
			if (pick.isStartActivity()) {
				amountOfHiddenStartActivities--;
			} else {
				addViolation(pick, PRECEDING_ACTIVITY_CREATES_NO_INSTANCE);
			}
			break;

		default:
			if (isActivity(activity.getLocalName())) {
				addViolation(activity, PRECEDING_ACTIVITY_CREATES_NO_INSTANCE);
			}
		}
	}

	private void analyzeFlow(NodeHelper activity) {
		if (activity.getFilePath().endsWith("sa00056_4.bpel")) {
			System.out.println("");
		}
		startBranches = new LinkedList<>();
		nonStartBranches = new LinkedList<>();

		for (ComparableNode childNode : listChildrenActivities(activity)) {
			Nodes starts = childNode.toXOM().query("descendant-or-self::*[@createInstance='yes']");
			NodeHelper childActivity = new NodeHelper(childNode);
			if (starts.hasAny()) {
				findStartActivities(childActivity, true);
				startBranches.add(childNode);
			} else {
				nonStartBranches.add(childNode);
			}
		}
		walkGraph(activity);
	}

	private void walkGraph(NodeHelper activity) {
		int startBranchGraphSize = startBranches.size();
		for (ComparableNode childNode : nonStartBranches) {
			if (isNoBranch(childNode)) {
				continue;
			}
			if (!isSucessorOfStartingBranch(childNode)) {
				addViolation(activity, FLOW_HAS_STARTING_AND_NON_STARTING_BRANCHES);
			}
		}
		restartWhenNonStartBranchesConnect(activity, startBranchGraphSize);
	}

	private void restartWhenNonStartBranchesConnect(NodeHelper activity, int startBranchGraphSize) {
		if (startBranchGraphSize < startBranches.size()) {
			walkGraph(activity);
		}
	}

	private boolean isNoBranch(ComparableNode childNode) {
		return !isActivity(new NodeHelper(childNode).getLocalName());
	}

	private boolean isSucessorOfStartingBranch(ComparableNode childActivity) {
		Nodes targetNodes = childActivity.toXOM().query("./bpel:targets/bpel:target",
				Standards.CONTEXT);
		if (!targetNodes.hasAny()) {
			return false;
		}
		for (Node targetNode : targetNodes) {
			TargetElement target = new TargetElement(targetNode, processContainer);
			try {
				NodeHelper source = new NodeHelper(target.getLink().getSourceElement());
				NodeHelper ancestor = source.getParent();
				while (!hasFoundBranchRoot(ancestor)) {
					ComparableNode branch = new ComparableNode(ancestor);
					if (nonStartBranches.contains(branch)) {
						isSucessorOfStartingBranch(branch);
					}
					ancestor = ancestor.getParent();
				}
				connectBranch(childActivity);
			} catch (NavigationException e) {
				return false;
			}
		}
		return true;
	}

	private void connectBranch(ComparableNode childActivity) {
		startBranches.add(childActivity);
		nonStartBranches.remove(childActivity);
	}

	private boolean hasFoundBranchRoot(NodeHelper ancestor) throws NavigationException {
		if ("process".equals(ancestor.getLocalName())) {
			throw new NavigationException("<source> is not in any start branch of the scope");
		}
		for (ComparableNode startBranch : startBranches) {
			if(startBranch.equals(ancestor)) {
				return true;
			}
		}
		return false;
	}

	private NodeHelper childActivity(Referable referable) {
		LinkedList<ComparableNode> elements = listChildrenActivities(referable);
		NodeHelper activity = new NodeHelper(elements.getLast().toXOM());
		return activity;
	}

	private boolean isActivity(String activityName) {
		for (String name : new String[] { "documentation", "targets", "sources", "links" }) {
			if (name.equals(activityName)) {
				return false;
			}
		}
		return true;
	}

	private void findStartActivitiesInHandler() {
		List<StartActivity> startActivities = processContainer.getAllStartActivities();
		for (StartActivity startActivity : startActivities) {
			NodeHelper ancestor = new NodeHelper(startActivity).getParent();
			while (!"process".equals(ancestor.getLocalName())) {
				if (!"scope".equals(ancestor.getLocalName())
						&& !"flow".equals(ancestor.getLocalName())
						&& !"sequence".equals(ancestor.getLocalName())
						&& !"pick".equals(ancestor.getLocalName())) {
					addViolation(ancestor, MUST_NOT_CONTAIN_START_ACTIVITY);
				}
				ancestor = ancestor.getParent();
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 56;
	}

}