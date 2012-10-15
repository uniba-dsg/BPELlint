#!/bin/bash

cd ..
for file in $(ls *.txt)
do
	mkdir temp
	cp ${file} temp/${file}.bu
	echo ${file}
	echo "#" | cat > $file
	cat temp/${file}.bu | cat >> $file
	rm -rf temp
done