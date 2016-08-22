#!/bin/bash

array=(01 02 03 09)
for ((i=0;i<=${#array[@]}-2;i++))
do
  echo "${array[$i]} - ${array[$i+1]}"
done

