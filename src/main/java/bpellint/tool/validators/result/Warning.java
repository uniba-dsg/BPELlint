package bpellint.tool.validators.result;

public class Warning extends Indicator{

	public final String message;

	public Warning(String fileName, int row, int column, String message) {
		super(fileName, row, column);
		this.message = message;
	}

}
