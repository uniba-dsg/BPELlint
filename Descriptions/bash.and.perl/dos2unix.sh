#!/bin/bash

# this script converts all line endings of all files present in the current
# directory to unix. (currently just doing it for .txt files, see first line.)
# it also echoes the file it is currently working on.
# the ':se nobomb' also removes the BOM!
# this previous line is another reason to just love this editor...

cd ..
for file in $(ls *txt)
do 
  echo ${file};
  vim +':se nobomb' +':w ++ff=unix' +':q' ${file};
done