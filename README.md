# ISABEL (I statically analyze BPEL files flawlessly)

The tool Isabel validates BPEL files according to 30 static analysis rules from the BPEL 2.0 specification.

This software is licensed under the LGPL Version 3 Open Source License.

## Software Requirements
- JDK 1.7.0_03 (64 Bit) or higher
  - `JAVA_HOME` should point to the jdk directory
  - `PATH` should include `JAVA_HOME/bin`

## Licensing
LGPL Version 3: http://www.gnu.org/licenses/lgpl-3.0.html

## Usage

Requirements (see above) have to be fulfilled to execute `isabel`.

```bash
$ isabel [options] <PATH>
# -f,--full                  Prints out the definitions of the violated rules as well

Examples:
$ isabel empty.bpel # Validates the <empty.bpel> file.
$ isabel folder # Validates *.bpel files within the <folder> and all its subfolders.

$ isabel -f empty.bpel # Validates the <empty.bpel> file including rule definition.
```

Only the .bpel file path or directory has to be given as parameter, all needed and referenced files will be loaded.
With no further parameters the output consists of error position and a short message.
Parameter order is not important, only the last used parameter is of importance.

## Development

```bash
$ gradlew test # run all unit tests
$ gradlew idea # Generating Intellij IDEA project files
$ gradlew eclipse # Generating Eclipse project files
$ gradlew javadoc # Generating JavaDoc
```

# Authors (in alphabetical order)

David Bimamisa, Christian Preissinger, Stephan Schuberth

Supervisor: [Simon Harrer](http://www.uni-bamberg.de/pi/team/harrer/)

# Contribution Guide

- Fork
- Send Pull Request