# BPELlint [![Build Status](https://travis-ci.org/uniba-dsg/BPELlint.png?branch=master)](https://travis-ci.org/uniba-dsg/BPELlint) [![Dependency Status](https://www.versioneye.com/user/projects/54c65c941a0071823a000284#tab-dependencies/badge.svg?style=flat)](https://www.versioneye.com/user/projects/54c65c941a0071823a000284#tab-dependencies) <img align="right" src="logos/BPELlint-logo.png" height="80" width="103"/>

The tool **BPELlint** validates BPEL files according to 71 static analysis rules from the BPEL 2.0 specification.

This software is licensed under the LGPL Version 3 Open Source License.

## Software Requirements
- JDK 1.8.0 (64 Bit) or higher
  - `JAVA_HOME` should point to the jdk directory
  - `PATH` should include `JAVA_HOME/bin`

## Licensing
Dual licensed under [LGPL Version 3](http://www.gnu.org/licenses/lgpl-3.0.html) and [EPL 1.0](http://opensource.org/licenses/EPL-1.0)

## Usage

Requirements (see above) have to be fulfilled to execute `BPELlint`.

```bash
usage: BPELlint [OPTIONS] PATH
PATH can be either a FILE or a DIRECTORY.

 -f,--full                   Prints out the definitions of the violated
                             rules as well.
 -h,--help                   Print usage information.
 -s,--no-schema-validation   Disables xsd schema validations.

Please report issues at https://github.com/uniba-dsg/BPELlint/issues

Examples:
$ BPELlint empty.bpel # Validates the <empty.bpel> file.
$ BPELlint folder # Validates *.bpel files within the <folder> and all its subfolders.

$ BPELlint -f empty.bpel # Validates the <empty.bpel> file including rule definition.
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

The folder `Testcases/betsy` is copied by hand for convenience from *betsy* `src/main/tests/files`.
Checkout https://github.com/uniba-dsg/betsy to get the latest version.

The environment variable `BPEL_LINT_SA_RULES` constrains the SA rules that are validated.
When `BPEL_LINT_SA_RULES` is not set or set to `all` then all SA rules are validated if set to `61,2,10` then SA00002, SA00010 and SA00061 are validated.

# Authors (in alphabetical order)

David Bimamisa, [Simon Harrer](http://www.uni-bamberg.de/pi/team/harrer/), Christian Preissinger, Stephan Schuberth

# Contribution Guide

- Fork
- Send Pull Request
