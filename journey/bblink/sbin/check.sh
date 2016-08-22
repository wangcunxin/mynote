#!/bin/sh
#source ~/.bashrc
date=$1
dat=`date '+%Y-%m-%d %H:%M:%S'`
echo "\n$dat"
echo $date
dat=`date +%Y%m%d`
dir=/devicelogs

for tb in 'acdeviceinfo' 'useronlineinfo' 'apinfo' 'userconnectedinfo' 'acinfo' 'usernetlog' 'userapplog' 'userdetectedinfo'
do
    ymd=$dir/$tb/$date
   
    count=`hadoop fs -ls $ymd |wc -l`
    echo $ymd	$(($count-1))
done










