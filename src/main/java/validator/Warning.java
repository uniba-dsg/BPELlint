package validator;

import java.util.Objects;

public final class Warning implements Comparable<Warning> {

    private final String message;
    private final Indicator indicator;

    public Warning(String message, Indicator indicator) {
        this.message = Objects.requireNonNull(message);
        this.indicator = Objects.requireNonNull(indicator);
    }

    public String getMessage() {
        return message;
    }

    public Indicator getIndicator() {
        return indicator;
    }

    @Override
    public String toString() {
        return "Warning{" +
                "message='" + message + '\'' +
                ", indicator=" + indicator +
                '}';
    }

    @Override
    public int compareTo(Warning o) {
        Objects.requireNonNull(o);

        if (getMessage().equals(o.getMessage())) {
            return getIndicator().compareTo(o.getIndicator());
        } else {
            return getMessage().compareTo(o.getMessage());
        }
    }
}
