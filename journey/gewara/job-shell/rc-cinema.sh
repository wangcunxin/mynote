#!/bin/bash

cd /opt/spark-work

begin=$1
end=$2
if [ "$begin" = "" ]; then
	begin=`date -d "yesterday" "+%F"`
	end=`date '+%Y-%m-%d'`
fi
echo "[$begin,$end)"
lib_home="/opt/gw-job/lib"

/usr/bin/spark-submit \
--master yarn \
--deploy-mode client \
--driver-memory 20G \
--executor-memory 6G \
--num-executors 10 \
--executor-cores 4 \
--jars $lib_home/ssdb4j-9.2.jar,$lib_home/mongo-java-driver-2.12.3.jar,$lib_home/hbase-common-1.0.3.jar \
--files /etc/hive/conf/hive-site.xml \
--class com.gewara.Job.riskcontrol.CinemaRiskControl \
/opt/spark-work/gespark.jar yarn-client $begin $end >>./rc-cenema.log

