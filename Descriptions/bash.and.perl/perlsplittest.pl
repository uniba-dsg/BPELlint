#!/bin/perl

$teststring1 = 'asdfsadf#poiupoiu';
$teststring2 = 'qwerqwer@lkjhlkjh';
$teststring3 = 'zxcvzxcv@@@mnbvmnbv';

@result1 = split('#', $teststring1);
@result2 = split('@', $teststring2);
@result3 = split('@@@', $teststring3);

print "result1:\n";
foreach (@result1) { print $_ . "\n"; }
print "result2:\n";
foreach (@result2) { print $_ . "\n"; }
print "result3:\n";
foreach (@result3) { print $_ . "\n"; }