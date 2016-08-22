dir=/root/works/python/tasks
logdir=/var/log/sparklog

kafkadir=$SPARK_HOME/lib/kafka
cd $kafkadir

$SPARK_HOME/bin/spark-submit \
--conf spark.cores.max=5 \
--jars spark-streaming-kafka_2.10-1.4.1.jar,kafka_2.10-0.8.2.0.jar,kafka-clients-0.8.2.0.jar,zookeeper-3.4.6.jar,zkclient-0.3.jar,metrics-core-2.2.0.jar \
$dir/spark_py/bblink/graphite/kafka_graphite_streaming.py \
"spark://master:7077" 60 &

