package bpellint.core.validators.rules;


import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import api.SimpleValidationResult;
import bpellint.core.model.NavigationException;
import bpellint.core.model.ProcessContainer;
import bpellint.core.model.bpel.CorrelationElement;
import bpellint.core.model.bpel.mex.StartActivity;

import com.google.common.collect.Sets;

public class SA00057Validator extends Validator {

	private static final int MISSING_CORRELATION = 2;
	private static final int NO_SHARED_CORRELATION_SET = 1;

	public SA00057Validator(ProcessContainer files,
			SimpleValidationResult validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<StartActivity> startActivities = processContainer.getAllStartActivities();
		if (isSingleStartingPoint(startActivities) || hasNoCorrelation(startActivities)) {
			return;
		}

		List<Set<String>> joinCorrelationSetNames = listAllJoinCorrelationSetNames(startActivities);
		haveAtLeastOneSharedCorrelationSet(joinCorrelationSetNames);
	}

	private boolean hasNoCorrelation(List<StartActivity> startActivities) {
		for (StartActivity startActivity : startActivities) {
			try {
				startActivity.getCorrelations();
				return false;
			} catch (NavigationException e) {
				// has no correlation => continue
			}
		}
		return true;
	}

	private boolean isSingleStartingPoint(List<StartActivity> startActivities) {
		return startActivities.size() == 1;
	}

	private List<Set<String>> listAllJoinCorrelationSetNames(List<StartActivity> startActivities) {
		List<Set<String>> joinCorrelationSetNames = new LinkedList<>();
		for (StartActivity receiveOrOnMessage : startActivities) {
			try {
				joinCorrelationSetNames.add(checkActivity(receiveOrOnMessage));
			} catch (NavigationException e) {
				addViolation(receiveOrOnMessage, MISSING_CORRELATION);
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
					addViolation(processContainer.getProcess(), NO_SHARED_CORRELATION_SET);
				}
			}
		}
	}

	@Override
	public int getSaNumber() {
		return 57;
	}

}
