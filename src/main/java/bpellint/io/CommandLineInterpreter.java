package bpellint.io;

import org.apache.commons.cli.*;

public class CommandLineInterpreter {

    public static final String NO_SCHEMA_VALIDATION = "no-schema-validation";
    public static final String HELP = "help";

    public CLIOptions parse(String[] args) throws ParseException {
        CommandLineParser parser = new BasicParser();
        try {
            CommandLine cmd = parser.parse(getOptions(), args);

            if(cmd.hasOption(HELP)){
                printUsage();
                System.exit(-1);
            }

            CLIOptions result = new CLIOptions();

            result.schemaValidation = !cmd.hasOption(NO_SCHEMA_VALIDATION);

            result.paths = cmd.getArgs();

            return result;
        } catch (ParseException e) {
            printUsage();
            throw e;
        }
    }

    private Options getOptions() {
        Options options = new Options();
        options.addOption("s", NO_SCHEMA_VALIDATION, false, "Disables xsd schema validations.");
        options.addOption("h", HELP, false, "Print usage information.");
        return options;
    }

    private void printUsage() {
        String firstLine = "BPELlint [OPTIONS] PATH";
        String header = "PATH can be either a FILE or a DIRECTORY.\n\n";
        String footer = "\nPlease report issues at https://github.com/BPELtools/BPELlint/issues";
        new HelpFormatter().printHelp(firstLine,
                header,
                getOptions(),
                footer);
    }

}
