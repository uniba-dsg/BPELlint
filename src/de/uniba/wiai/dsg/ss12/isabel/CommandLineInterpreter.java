package de.uniba.wiai.dsg.ss12.isabel;

import de.uniba.wiai.dsg.ss12.isabel.tool.VerbosityLevel;

public class CommandLineInterpreter {
	public final VerbosityLevel verbosityLevel;
	public final String bpel_file;

	public CommandLineInterpreter(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException();
		}

		VerbosityLevel verbosityLevel = VerbosityLevel.NORMAL;
		String bpel_file = "";

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
					bpel_file = string;
			}
		}

		this.verbosityLevel = verbosityLevel;
		this.bpel_file = bpel_file;
	}
}
