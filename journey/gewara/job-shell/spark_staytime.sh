CURRENTDATE=`date -d 'yesterday' "+%F"`

echo `date "+%F %X"`" : ==start compute usertrace=="
`spark-submit --class com.gewara.Job.UserTraceFromHdfsJob --master yarn --driver-memory 20G --executor-memory 6G --num-executors 10 --executor-cores 4 --jars  /opt/gw-job/lib/ssdb4j-9.2.jar --files /etc/hive/conf/hive-site.xml /opt/spark-work/gewa-spark.jar /user/sqoop/accesslog/ $CURRENTDATE 1`
echo `date "+%F %X"`" : ==end compute usertrace=="
echo "======================================================"

echo `date "+%F %X"`" : ==start compute page ava staytime =="
`spark-submit --class com.gewara.Job.PageAvgTime  --master yarn --driver-memory 20G --executor-memory 6G --num-executors 10 --executor-cores 4 --jars /opt/gw-job/lib/ssdb4j-9.2.jar --files /etc/hive/conf/hive-site.xml /opt/spark-work/gewa-spark.jar $CURRENTDATE 1`
echo `date "+%F %X"`" : ==end compute page ava staytime =="

echo "======================================================"

echo `date "+%F %X"`" : ==start compute page ava staytime with refer=="
`spark-submit --class com.gewara.Job.PageAvgTimeWithRef --master yarn --driver-memory 20G --executor-memory 6G --num-executors 10 --executor-cores 4 --jars /opt/gw-job/lib/ssdb4j-9.2.jar --files /etc/hive/conf/hive-site.xml /opt/spark-work/gewa-spark.jar $CURRENTDATE 1`
echo `date "+%F %X"`" : ==end compute page ava staytime with refer=="

echo "======================================================"

