#!/bin/bash
ym=201512
#array=(01 02 03 04 05 06 07 08 09 10)
array=(08 09 10 11 12 13)

for ((i=0;i<=${#array[@]}-2;i++))
do
  begin=$ym"${array[$i]}"
  end=$ym"${array[$i+1]}"
  echo $begin $end
  #bash mysql2parquet_loginflow.sh $begin $end &
  bash mysql2parquet_wechatflow.sh $begin $end &
done


