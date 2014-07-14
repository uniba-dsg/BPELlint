package bpellint.tool.validators.result;

public class Violation extends Indicator {

	public final int ruleNumber;
	public final int type;

	public Violation(String fileName, int ruleNumber, int type, int row,
	                 int column) {
		super(fileName, row, column);
		this.ruleNumber = ruleNumber;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Violation{" + "ruleNumber=" + ruleNumber + ", row=" + row
				+ ", column=" + column + ", type=" + type + ", fileName='"
				+ fileName + '\'' + '}';
	}
}
