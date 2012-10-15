#!/bin/perl


$sw = 1;

while ($sw <=95) {

	if ($sw != 49) {
	
		if ($sw <10) {
		 	print "%\\input{texfiles/SA0000" . $sw . "} ";
		}
		elsif (($sw > 9 ) && ($sw < 96)){
		 	print "%\\input{texfiles/SA000" . $sw . "} ";
		}
		
		print "\\clearpage\n";
	}
	
	 	$sw++;
}
