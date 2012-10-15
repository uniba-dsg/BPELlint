package de.uniba.wiai.dsg.ss12.isabel.tool;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 5647199707160120246L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(Throwable throwable, String message) {
		super(message, throwable);
	}

	public ValidationException(Throwable throwable) {
		super(throwable);
	}
}
