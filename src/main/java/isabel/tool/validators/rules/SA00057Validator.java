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

	private int correlationgStartActivitiesCounter;

	public SA00057Validator(ProcessContainer files,
			ValidationCollector validationCollector) {
		super(files, validationCollector);
	}

	@Override
	public void validate() {
		List<Set<String>> joinCorrelationSetNames = new LinkedList<>();
		correlationgStartActivitiesCounter = 0;
		for (StartActivity receiveOrOnMessage : fileHandler.getAllStartAcivities()) {
			try {
				joinCorrelationSetNames.add(checkActivity(receiveOrOnMessage));
			} catch (NavigationException e) {
				// no starting point with correlation => continue with next start activity
			}
		}
		
		if (correlationgStartActivitiesCounter < 2) {
			return;
		}	
		for (Set<String> setA : joinCorrelationSetNames) {
			for (Set<String> setB : joinCorrelationSetNames) {
				if (!(Sets.intersection(setA, setB).size() > 0)) {
					addViolation(fileHandler.getProcess());
				}
			}
		}

		
	}

	private Set<String> checkActivity(StartActivity receiveOrOnMessage) throws NavigationException {
		if (receiveOrOnMessage.isStartActivity()) {
			Set<String> activityJoinCorrelationSetNames = new HashSet<>();
			for (CorrelationElement correlation : receiveOrOnMessage.getCorrelations()) {
				if (correlation.isJoinInitiate()) {
					activityJoinCorrelationSetNames.add(correlation.getCorrelationSet());
				}
			}
			correlationgStartActivitiesCounter++;
			return activityJoinCorrelationSetNames;
		} else {
			throw new NavigationException("This is not a start activity.");
		}
	}

	@Override
	public int getSaNumber() {
		return 57;
	}

}
