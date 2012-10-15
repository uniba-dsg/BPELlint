#!/bin/bash

### before usage:
### move pseudocodenotation.txt to a file with another extension
### so the script wont fuck up the comments!

cd ..
mkdir temp

for file in $(ls *txt)
do 
  echo ${file};
  
  sed 's/^@@@@@/@@@/' <${file} >temp/$file
  mv temp/$file $file
done

rm -rf temp