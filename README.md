HOW TO USE ISABEL
=================

ISABEL can be used from the commandline in the form of:

$ java -jar isabel.jar file.bpel [-f] [-v] [--full] [--verbose]


Only the .bpel file path has to be given as parameter, all needed and referenced files will be loaded. 
With no further parameters the output consists of error position and a short message.	
Parameter order is not important, only the last used parameter is of importance.
	
	
Optional parameters:
	
	-f
		Same as --full.
		
	--full
		Output consists of the position of the error, a short specific message and the actual SA rule.
	
	-v 
		Same as --verbose.
	
	--verbose
		The position of the error and the description of the actual SA rule are given back.

Other Tasks

$ gradlew run -Pargs="empty.bpel" // validates the empty.bpel file directly
$ gradlew test // run all unit tests
$ gradlew javadoc // generate JavaDoc
$ gradlew eclipse // generate Eclipse project files (under some circumstances it is required to fix the jdk import in the eclipse build path)