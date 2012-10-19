package de.uniba.wiai.dsg.ss12.isabel.io;

public class CommandLineInterpreter {
	public final VerbosityLevel verbosityLevel;
	public final String bpelFile;

	public CommandLineInterpreter(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException();
		}

		VerbosityLevel verbosityLevel = VerbosityLevel.NORMAL;
		String bpelFile = "";

		for (String string : args) {
			switch (string) {
				case "--verbose":
				case "-v":
					verbosityLevel = VerbosityLevel.VERBOSE;
					break;
				case "--full":
				case "-f":
					verbosityLevel = VerbosityLevel.FULL;
					break;
				default:
					bpelFile = string;
			}
		}

		this.verbosityLevel = verbosityLevel;
		this.bpelFile = bpelFile;
	}
}
