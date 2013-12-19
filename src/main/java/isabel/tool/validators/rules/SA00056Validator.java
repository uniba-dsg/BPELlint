package isabel.tool.validators.rules;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.google.common.collect.Sets;

import isabel.model.ComparableNode;
import isabel.model.NodeHelper;
import isabel.model.ProcessContainer;
import isabel.model.bpel.PickElement;
import isabel.model.bpel.mex.ReceiveElement;
import isabel.tool.impl.ValidationCollector;

public class SA00056Validator extends Validator {

	private TreeSet<ComparableNode> dom;
	private List<ComparableNode> startingFlows;

	public SA00056Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		dom = new TreeSet<>();
		startingFlows = new LinkedList<>();
		List<ComparableNode> startActivities = new LinkedList<>();
		Set<ComparableNode> otherReceiveActivities = new HashSet<>();
		
		for (ReceiveElement receive : fileHandler.getAllReceives()) {
			ComparableNode comparableActivity = new ComparableNode(receive.toXOM());
			dom.add(comparableActivity);
			if (receive.isStartActivity()) {
				checkAncestorStartActivities(receive);
				startActivities.add(comparableActivity);
			} else {
				listAncestors(receive);
				otherReceiveActivities.add(comparableActivity);
			}
		}
		for (PickElement pick : fileHandler.getAllPicks()) {
			ComparableNode comparableActivity = new ComparableNode(pick.toXOM());
			dom.add(comparableActivity);
			NodeHelper nodeHelper = new NodeHelper(pick.toXOM());
			if (pick.isStartActivity()) {
				checkAncestorStartActivities(nodeHelper);
				startActivities.add(comparableActivity);
			} else {
				listAncestors(nodeHelper);
				otherReceiveActivities.add(comparableActivity);
			}
		}
		
		for (ComparableNode startActivity : startActivities) {
			SortedSet<ComparableNode> headSet = dom.headSet(startActivity);
			if (!Sets.intersection(otherReceiveActivities, headSet).isEmpty()){
				addViolation(startActivity, 2);
			}
		}
	}

	private void listAncestors(NodeHelper startActivity) {
		NodeHelper node = startActivity;
		while (!"process".equals(node.getParent().getLocalName())) {
			node = node.getParent();
			dom.add(new ComparableNode(node.toXOM()));
		} 
	}

	private void checkAncestorStartActivities(NodeHelper startActivity) {
		NodeHelper node = startActivity;
		while (!"process".equals(node.getParent().getLocalName())) {
			node = node.getParent();
			ComparableNode comparableNode = new ComparableNode(node.toXOM());
			dom.add(comparableNode);
			if ("scope".equals(node.getLocalName())) {
				continue;
			} else if ("pick".equals(node.getLocalName()) && new PickElement(node).isStartActivity()) {
				continue;
			} else if ("sequence".equals(node.getLocalName())) {
				continue;
			} else if ("flow".equals(node.getLocalName())) {
				startingFlows.add(comparableNode);
				continue;
			} else {
				addViolation(node, 1);
				break;
			}
		} 
	}

	@Override
	public int getSaNumber() {
		return 56;
	}

}
