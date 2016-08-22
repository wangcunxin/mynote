#!/bin/sh
dir=/home/hadoop/cxwang.kevin/works
date=`date -d "-1 day" +%Y%m%d`
sh $dir/check/check.sh $date >>$dir/logs/check.log
