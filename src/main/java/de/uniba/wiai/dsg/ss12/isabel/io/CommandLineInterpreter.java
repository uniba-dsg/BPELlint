package de.uniba.wiai.dsg.ss12.isabel.io;

public class CommandLineInterpreter {
	public static final String USAGE = "Usage: [--verbose|--full] PATH\nPATH can be a BPEL file or a Folder containing BPEL files";
	public final VerbosityLevel verbosityLevel;
	public final String path;

	public CommandLineInterpreter(String[] args) {
		if (args == null || args.length == 0) {
			throw new IllegalArgumentException(USAGE);
		}

		VerbosityLevel verbosityLevel = VerbosityLevel.NORMAL;
		String path = "";

		for (String string : args) {
			switch (string) {
				case "--full":
				case "-f":
					verbosityLevel = VerbosityLevel.FULL;
					break;
				default:
					path = string;
			}
		}

		if(path.isEmpty()){
			throw new IllegalArgumentException(USAGE);
		}

		this.verbosityLevel = verbosityLevel;
		this.path = path;
	}

}
