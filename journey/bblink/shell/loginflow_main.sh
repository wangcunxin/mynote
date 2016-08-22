#!/bin/bash
dir=/root/works/python/tasks
logdir=/var/log/sparklog
ymd=`date -d "-1 day" +%Y%m%d`

/opt/cloud/spark/bin/spark-submit \
--master yarn-client \
--executor-memory 13g \
--driver-memory 10g \
--conf spark.cores.max=25 \
$dir/spark_py/bblink/back/hossubject_logincount.py \
$ymd \
spark://master:7077 >> $logdir/hossubject_logincount.log &

