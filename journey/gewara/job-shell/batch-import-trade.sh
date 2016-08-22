#!/bin/bash

## 进入hdfs的home目录，执行后续脚本
cd ~

#if [ $# -lt 2 ] ;then
# echo 'please input date and range!'
 #exit
#fi

#start=$1
#range=$2

#for ((i=0;i<$range;i++));
#do
runday=`date -d "yesterday" "+%F"`
#t=`date  '+%Y-%m-%d %H:%M:%S'`
#echo $t":odl_trade pull data for "$runday
/opt/spark-work/import-trade.sh $runday

#done

