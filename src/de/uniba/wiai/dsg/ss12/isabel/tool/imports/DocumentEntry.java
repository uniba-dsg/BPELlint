package de.uniba.wiai.dsg.ss12.isabel.tool.imports;

import nu.xom.Document;

public class DocumentEntry {

	private final String filePath;
	private final Document document;
	private final String targetNamespace;

	public DocumentEntry(String filePath, String targetNamespace,
			Document document) {
		if (filePath == null || targetNamespace == null || document == null) {
			throw new IllegalArgumentException(
					"None of the input parameter must be null.");
		}

		this.filePath = filePath;
		this.targetNamespace = targetNamespace;
		this.document = document;
	}

	public String getFilePath() {
		return filePath;
	}

	public Document getDocument() {
		return document;
	}

	public String getTargetNamespace() {
		return targetNamespace;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((filePath == null) ? 0 : filePath.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentEntry other = (DocumentEntry) obj;
		if (filePath == null) {
			if (other.filePath != null)
				return false;
		} else if (!filePath.equals(other.filePath))
			return false;
		return true;
	}

}
