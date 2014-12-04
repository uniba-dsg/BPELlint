package validator;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

public final class Indicator implements Comparable<Indicator> {

	private final Optional<Location> location;
	private final Optional<String> xpath;

	private final Path fileName;

	public Indicator(Path fileName, Location location) {
		this.fileName = Objects.requireNonNull(fileName);
		this.location = Optional.of(location);
		this.xpath = Optional.empty();
	}

	public Location getLocation() {
		return location.orElse(Location.EMPTY);
	}

	public Optional<String> getXpath() {
		return xpath;
	}

	public Path getFileName() {
		return fileName;
	}

	@Override
	public int compareTo(Indicator o) {
		Objects.requireNonNull(o);

		if (isSameFile(o)) {
			return getLocation().getId().compareTo(o.getLocation().getId());
		} else {
			return fileName.compareTo(o.fileName);
		}
	}

	private boolean isSameFile(Indicator o) {
		return fileName.equals(o.fileName);
	}

}
