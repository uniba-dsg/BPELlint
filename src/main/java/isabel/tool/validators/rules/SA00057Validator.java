package isabel.tool.validators.rules;

import isabel.model.NavigationException;
import isabel.model.ProcessContainer;
import isabel.model.bpel.CorrelationElement;
import isabel.model.bpel.mex.StartActivity;
import isabel.tool.impl.ValidationCollector;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class SA00057Validator extends Validator {

	public SA00057Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<StartActivity> startActivities = processContainer.getAllStartActivities();
		List<Set<String>> joinCorrelationSetNames = listAllJoinCorrelationSetNames(startActivities);

		if (isSingleStartingPoint(joinCorrelationSetNames)) {
			return;
		}
		haveAtLeastOneSharedCorrelationSet(joinCorrelationSetNames);
	}

	private boolean isSingleStartingPoint(List<Set<String>> joinCorrelationSetNames) {
		// There might be no start activity with correlation set
		return joinCorrelationSetNames.size() <= 1;
	}

	private List<Set<String>> listAllJoinCorrelationSetNames(List<StartActivity> startActivities) {
		List<Set<String>> joinCorrelationSetNames = new LinkedList<>();
		for (StartActivity receiveOrOnMessage : startActivities) {
			try {
				joinCorrelationSetNames.add(checkActivity(receiveOrOnMessage));
			} catch (NavigationException e) {
				// no starting point with correlation => continue with next start activity
			}
		}
		return joinCorrelationSetNames;
	}

	private Set<String> checkActivity(StartActivity receiveOrOnMessage) throws NavigationException {
		if (!receiveOrOnMessage.isStartActivity()) {
			throw new NavigationException("This is not a start activity.");
		}
		Set<String> activityJoinCorrelationSetNames = new HashSet<>();
		for (CorrelationElement correlation : receiveOrOnMessage.getCorrelations()) {
			if (correlation.isJoinInitiate()) {
				activityJoinCorrelationSetNames.add(correlation.getCorrelationSetAttribute());
			}
		}
		return activityJoinCorrelationSetNames;
	}

	private void haveAtLeastOneSharedCorrelationSet(
			List<Set<String>> joinCorrelationSetNames) {
		for (Set<String> setA : joinCorrelationSetNames) {
			for (Set<String> setB : joinCorrelationSetNames) {
				if (!(Sets.intersection(setA, setB).size() > 0)) {
					addViolation(processContainer.getProcess());
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 57;
	}

}
