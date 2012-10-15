#!/bin/bash


cd ..
for file in $(ls *.txt)
do
	mkdir temp
	cp ${file} temp/${file}.bu
	echo ${file}
	sed '1d' temp/${file}.bu > $file
	rm -rf temp
done