package de.uniba.wiai.dsg.ss12.isabel.tool;

public class Violation implements Comparable<Violation> {

	public final int ruleNumber;
	public final int row;
	public final int column;
	public final int type;
	public final String fileName;

	public Violation(String fileName, int ruleNumber, int type, int row,
			int column) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName must not be null");
		}

		this.fileName = fileName;
		this.ruleNumber = ruleNumber;
		this.row = row;
		this.column = column;
		this.type = type;
	}

	@Override
	public int compareTo(Violation o) {
		if (isSameFile(o)) {
			if (isSameRow(o)) {
				return column - o.column;
			} else {
				return (row - o.row);
			}
		} else {
			return (fileName.hashCode() - o.fileName.hashCode());
		}
	}

	private boolean isSameFile(Violation o) {
		return fileName.equals(o.fileName);
	}

	private boolean isSameRow(Violation o) {
		return row == o.row;
	}

	@Override
	public String toString() {
		return "Violation{" +
				"ruleNumber=" + ruleNumber +
				", row=" + row +
				", column=" + column +
				", type=" + type +
				", fileName='" + fileName + '\'' +
				'}';
	}
}
