#!/bin/bash

# writes all subroutines of all SA files into file 'helpermethods-list.txt'
# in the same directory
# it afterwards prints the contents of the file to the console

cd ..
grep -w '\->' SA*.txt | cat > helpermethods-list.txt

cat helpermethods-list.txt