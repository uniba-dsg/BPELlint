#!/bin/perl
#use warnings;
#use strict;

# SCRIPT TO parse the SA000xx.txt files for their four information blocks, delimited by '#':
#
# blocks are:
#		- short (short description of the violated SA rule)
#		- long (complete description of SA rule taken from the standard)
#		- algorithm (pseudocode description on how to implement rule check)
#		- test (what to keep in mind when creating a test case for the rule checker)
#
# Also gets a list of test cases present currently in the corresponding
# /Testcases/SA000xx folder.
#
# Finally outputs .tex files into /Descriptions/texfiles for the documentation
# - outputs SHORT, LONG, ALGORITHM, TESTS, TESTFILES into .tex file
# Finally outputs errormessages.xml for the tool itself into tool's root package /bpelval
# - outputs SHORT's (if there are several depending on \newlines present in SHORT block)
#   into single tags and the LONG.



# GET INPUT FILES LIST

	# open current dir as source dir
	$dirToGetFilesFrom = "..";
	opendir(SRCDIR, $dirToGetFilesFrom) or die $!; 
	
	# get contents
	@thefiles = readdir(SRCDIR); 
	
	# close source dir
	closedir(SRCDIR);
	
	
	# initiate the xml file before the loop
	# because there is only one file, not like 1000's of .tex files...
	$fn_xml = "../../resources/errormessages.xml";
	#first overwrite and dont append
	open XMLOUT, ">$fn_xml";
	
	# xml head declaration
	$rs_xml_head = "<?xml version='1.0' encoding='UTF-8'?>\n\n";
	$rs_xml_head .= "<rules\n";
	$rs_xml_head .= "xmlns='http://lspi.wiai.uni-bamberg.de/ed-12-ss-proj-bpelval/schema/errormessages'\n";
	$rs_xml_head .= "xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'\n"; 
	$rs_xml_head .= "xsi:schemaLocation='http://lspi.wiai.uni-bamberg.de/ed-12-ss-proj-bpelval/schema/errormessages errormessages.xsd'\n"; 
	$rs_xml_head .= ">\n\n";
	
	
	print XMLOUT $rs_xml_head;
	close XMLOUT;
	
	
# GET AVAILABLE CORRESPONDING TEST CASE DIRS
				
	# open Testcases root dir relative to current dir
	$testcasesRootDir = "../../Testcases";
	opendir(TESTCASEROOTDIR, $testcasesRootDir) || die("Cannot open testcases directory"); 
					
	# get contents
	@testfiledirs = readdir(TESTCASEROOTDIR); 
					
	# close Testcases root dir
	closedir(TESTCASEROOTDIR);
					

# ITERATE OVER FILES

	foreach my $file (@thefiles)
	{
		#check if filename contains 'SA000' substring
		#to see if the file should be parsed
		#index() returns values >= 0 if string is found
		if (index($file, "SA000") >= 0) 
		{
		
			if (index($file, ".txt") >= 0)
			{
			
			
# GET DATA
		
					#open inputfile
					open INPUT, "../$file" or die $!;
			
					# reset $inputstring and @presentTestFiles
					$inputstring = '';
					@presentTestFiles = ();
					
					#read whole file
					while (<INPUT>) 
					{
						$inputstring = $inputstring . "$_";
					}
			
					#close inputfile
					close INPUT;
		
		
# GET INFO TO WORK WITH (current filename)
		
					#get filename without extension into '$filename[0]'
					@filename = split('\.', $file);
					$fn = $filename[0];
					
					# get all testfiles to corresponding SA
					# get the /Testcases/SA000xx folders currently present
					@availableTestDirs = grep /$fn/, @testfiledirs;
					
					# if SA000xx folder and thus tests exist...
					if (grep { $_ eq $fn } @availableTestDirs)
					{
					
						# open current testcase dir
						$testcaseDir = "../../Testcases/$fn";
						opendir(TESTCASEDIR, $testcaseDir) || die("Cannot open current testcase directory"); 
											
						# get contents
						@presentTestFiles = readdir(TESTCASEDIR); 
											
						# close current Testcase dir
						closedir(TESTCASEDIR);
						
					}
					else 
					{
						push(@presentTestFiles, "No testcases present.");
					}
					
					# remove '.' and '..' folder entries if needed
					# that is, if array has not less than two elements
					if (!(($#presentTestFiles + 1) < 2)) 
					{ 
						shift @presentTestFiles;
						shift @presentTestFiles;
					}
				
				
# WORK ON DATA
				
					#split the file's contents
					@content = split('\@\@\@', $inputstring);
			
					$short 			= $content[0];
					$long 			= $content[1];
					$algorithm	 	= $content[2];
					$test 			= $content[3];
					$syah			= $content[4]; # 'specify your algorithm here' = pseudocode description
					
					#sanitize values, means kill appended and prepended \newlines
						# prepended
							$revshort = reverse($short);
							chomp($revshort);
							$short = reverse($revshort);
							$revlong = reverse($long);
							chomp($revlong);
							$long = reverse($revlong);
							$revalgorithm = reverse($algorithm);
							chomp($revalgorithm);
							$algorithm = reverse($revalgorithm);
							$revtest = reverse($test);
							chomp($revtest);
							$test = reverse($revtest);
							$revsyah = reverse($syah);
							chomp($revsyah);
							$syah = reverse($revsyah);
						# appended
							chomp($short);
							chomp($long);
							chomp($algorithm);
							chomp($test);
							chomp($syah);
					
					#check if $short is consisting of several entries
					#a.k.a. split short string around \newlines
					@shorts = split('\n', $short);
					
					
										#	#testcode to see if all files have a empty firstline?
										#	print "#############################\n";
										#	foreach (@shorts) 
										#	{
										#		print $_;
										#		print "\n";
										#		#print "XXXX \n";
										#	}
										#	print "###### NO MORE SHORTS #######\n\n";
			
					
					#generate the string parts to be concatenated later
					#short messages are defined earliest, because it depends
					#on how many are there...
# from here to the next if short switch be dragons. mess with much caution!
					#generate for single-short-case
					$latexshort 	= "\\item short description(s):\\\\\n" . $short;
					$xmlshort 		= "<shorts>\n\t\t\t"	. $short . "\n\t\t</shorts>\n";
						# but if there are several short messages...
						# ... generate multi-short-case and ...
						# ... replace the regular short message!
						
# next line switches <subshort> tagging
	 # > -1 equals all shorts put in subshorts
	 # > 0 equals short just with at least two shorts are 'subshorted'
	 	# check the latex file output after switching this!!!
	 	
							if (($#shorts) > -1)
							{
								# generate enumeration of shorts in latex
								$latexshorts 	= "\t\\item \\textit{Short description(s):}\n\t\t\\begin{itemize}";
								$xmlshorts 		= "<shorts>\n";
								$counter = 0;
								foreach (@shorts) 
								{
									$counter += 1;
									$latexshorts .= "\n\t\t\t\\item " . $_;	
									$xmlshorts 	.= "\t\t\t<type>\n";
									$xmlshorts 	.= "\t\t\t\t<number>\n";
									$xmlshorts 	.= "\t\t\t\t\t" . $counter . "\n";
									$xmlshorts 	.= "\t\t\t\t</number>\n";
									$xmlshorts 	.= "\t\t\t\t<content>\n";
									$xmlshorts 	.= "\t\t\t\t\t<![CDATA[" . $_ . "]]>\n";
									$xmlshorts 	.= "\t\t\t\t</content>\n";
									$xmlshorts	.= "\t\t\t\</type>\n";
								}
								$latexshorts 	.= "\n\t\t\\end{itemize}\n";
								$xmlshorts		.= "\t\t</shorts>\n";
								
								
								# replace single-short-case parts already defined
								$latexshort = $latexshorts;
								$xmlshort = $xmlshorts;
							}
					$latexlong 		= "\t\\item \\textit{SA specification:}\\\\\n\t\t\t" . $long . "\n";
			# fix for sa27 because single '#' cannot be viewed in latex...
			# substitution will happen in $latexshort and $latexlong
					if ($fn eq 'SA00027') {
						$latexshort =~ s/\#/\\\#/g;
						$latexlong =~ s/\#/\\\#/g;
					}
					$xmllong 		= "<long>\n\t\t\t<![CDATA[" . $long . "]]>\n\t\t</long>\n";
					# remove the '\\' needed for latex again from the xml strings...
						$xmlshort =~ s/\\\\//g;
						$xmllong =~ s/\\\\//g;
					$latexalgorithm = "\t\\item \\textit{Algorithm:}\n\n\n\t\t\\begin{pseudocode}\n" . $algorithm . "\n\t\t\\end{pseudocode}\n\n\n";
					$latexsyah 		= "\t\\item \\textit{Algorithm description:}\\\\\n" . $syah . "\n\n\n";
			# do pretty formatting on testcases
					$latextest 		= "\t\\item \\textit{Test case description(s):}\n";
					@testsplit = split('\n', $test);
					$testsplitcounter = 0;
					foreach (@testsplit)
					{
						
						if ($testsplitcounter % 2 == 0)
						{
							$latextest .= "\\\\\t\t\\textbf{" . $_ . "}\\\\\n";
							$testsplitcounter = $testsplitcounter + 1;
						}
						else
						{
							$latextest .= "\t\t" . $_ . "\n";
							$testsplitcounter = $testsplitcounter + 1;
						}
					}
					
					# generate listing of testfiles
					# currently not used, see below where $rs_tex is generated
					$latextestfiles = "\t\\item corresponding test files:\n\t\t\\begin{itemize}";
					foreach (@presentTestFiles) 
					{	
						$latextestfiles .= "\n\t\t\t\\item " . $_;
					}
					$latextestfiles .= "\n\t\t\\end{itemize}";
					# CURRENTLY UNNEEDED, don't forget to put additional tabs into the code if you want to actually use it
					#$xmlalgorithm 	= "<algorithm>" . $algorithm . "\n</algorithm>\n";
					#$xmltest 		= "<test>" 		. $test . "\n</test>\n";
			
			
					#start to real result strings to be written
					#latex
						$rs_tex = "\\section{" . $fn . "}\\label{". $fn . "}\n\n";
						# commented out the testfiles shown at the end for easier comparing
						$rs_tex .= "\\begin{itemize}\n" . $latexshort . $latexlong . $latexalgorithm . $latexsyah . $latextest;
						#rs_tex .= $latextestfiles;
						$rs_tex .= "\n";
						$rs_tex .= "\\end{itemize}\n";
					#xml
						#first empty the variable before creating the string 
						$rs_xml = "";
						$rs_xml .= "\t<rule name='" . $fn . "'>\n";
						$rs_xml .= "\t\t" . $xmlshort;
						$rs_xml .= "\t\t" . $xmllong;
						$rs_xml .= "\t</rule>\n";
				
					#write to .tex file
					#there is a single .tex file for every SA000xx!
					#no append, just overwrite it every time
					$fn_tex = "./../../../../bpelval-doku/texfiles/" . $fn . ".tex";
					open LATEXOUTPUT, ">$fn_tex" or DIE $!;
					print LATEXOUTPUT $rs_tex;
					close LATEXOUTPUT;
				
					#append to .xml file
					#got deleted before the foreach loop
					open XMLOUT, ">>$fn_xml";
					print XMLOUT $rs_xml;
					close XMLOUT;
			}
	
		}
	
	}


# FINISH XML FILE (append the rest)

	$rs_xml_tail = "</rules>";
	open XMLOUT, ">>$fn_xml";
	print XMLOUT $rs_xml_tail;
	close XMLOUT;
