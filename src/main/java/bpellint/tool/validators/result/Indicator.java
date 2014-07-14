package bpellint.tool.validators.result;

public class Indicator implements Comparable<Indicator> {

	public final int row;
	public final int column;
	public final String fileName;

	Indicator(String fileName, int row, int column) {
		if (fileName == null) {
			throw new IllegalArgumentException("fileName must not be null");
		}

		this.fileName = fileName;
		this.row = row;
		this.column = column;
	}

	@Override
	public int compareTo(Indicator o) {
		if (o == null) {
			throw new NullPointerException("cannot compare to null");
		}

		if (isSameFile(o)) {
			if (isSameRow(o)) {
				return Integer.compare(column, o.column);
			} else {
				return Integer.compare(row, o.row);
			}
		} else {
			return fileName.compareTo(o.fileName);
		}
	}

	private boolean isSameFile(Indicator o) {
		return fileName.equals(o.fileName);
	}

	private boolean isSameRow(Indicator o) {
		return row == o.row;
	}

}
