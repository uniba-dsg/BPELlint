package isabel.model.bpel;

public class OptionalElementNotPresentException extends Exception {

	private static final long serialVersionUID = -9099211837033956452L;

	public OptionalElementNotPresentException(String message, Throwable cause) {
		super(message, cause);
	}

	public OptionalElementNotPresentException(String message) {
		super(message);
	}

}
