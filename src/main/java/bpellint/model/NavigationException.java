package bpellint.model;

public class NavigationException extends Exception {

	private static final long serialVersionUID = -4872205297169941744L;

	public NavigationException() {
		super();
	}

	public NavigationException(String message) {
		super(message);
	}

	public NavigationException(Throwable throwable, String message) {
		super(message, throwable);
	}

	public NavigationException(Throwable throwable) {
		super(throwable);
	}
}
