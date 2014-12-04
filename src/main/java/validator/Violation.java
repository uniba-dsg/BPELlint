package validator;

import java.util.Objects;

public final class Violation implements Comparable<Violation> {

	private final String constraint;
	private final String message;
	private final Indicator indicator;

	public Violation(Indicator indicator, String message, String constraint) {
		this.indicator = Objects.requireNonNull(indicator);
		this.message = Objects.requireNonNull(message);
		this.constraint = Objects.requireNonNull(constraint);
	}

	public String getConstraint() {
		return constraint;
	}

	public String getMessage() {
		return message;
	}

	public Indicator getIndicator() {
		return indicator;
	}

	@Override
	public String toString() {
		return "Violation{" +
				"constraint='" + constraint + '\'' +
				", message='" + message + '\'' +
				", indicator=" + indicator +
				'}';
	}

	@Override
	public int compareTo(Violation o) {
		Objects.requireNonNull(o);

		if(getConstraint().equals(o.getConstraint())){
			if(getMessage().equals(o.getMessage())) {
				return getIndicator().compareTo(o.getIndicator());
			} else {
				return getMessage().compareTo(o.getMessage());
			}
		} else {
			return getConstraint().compareTo(o.getConstraint());
		}
	}

}
