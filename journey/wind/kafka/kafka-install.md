1.��http://kafka.apache.org/����kafka��װ����
2.tar zxvf kafka_2.8.0.tar.gz,�޸������ļ�conf/server.properties��
broker.id=0��host.name=10.100.5.9��zookeeper.connect=10.100.5.9:2181�ɶ��ŷָ����ö��
3.��������bin/zkServer.sh start�� bin/kafka-server-start.sh config/server.properties &
4.����topic���⣺
bin/kafka-create-topic.sh --zookeeper 10.100.5.9:2181 --replica 3 --partition 1 --topic mykafka
//��������Unrecognized VM option '+UseCompressedOops'
�鿴 bin/kafka-run-class.sh
�ҵ�
if [ -z "$KAFKA_JVM_PERFORMANCE_OPTS" ]; then
  KAFKA_JVM_PERFORMANCE_OPTS="-server  -XX:+UseCompressedOops -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark -XX:+DisableExplicitGC -Djava.awt.headless=true"
fi
ȥ��-XX:+UseCompressedOops
5.�������⣺
bin/kafka-list-topic.sh --zookeeper 10.100.5.9:2181
6.�����߷�����Ϣ��
bin/kafka-console-producer.sh --broker-list 10.100.5.9:9092 --topic mykafka
7.�����߽�����Ϣ��
bin/kafka-console-consumer.sh --zookeeper 10.100.5.9:2181 --topic mykafka --from-beginning
8.��broker����
֮ǰ��һ̨�����ϵ���һ��node�����ڳ�����һ̨�����Ϸ�3��node����broker
�޸������ļ�
# cp config/server.properties config/server-1.properties
# cp config/server.properties config/server-2.properties
config/server-1.properties:
    broker.id=1
    port=9093
    log.dir=/tmp/kafka-logs-1
 
config/server-2.properties:
    broker.id=2
    port=9094
    log.dir=/tmp/kafka-logs-2
����broker1��broker2
# JMX_PORT=9997 bin/kafka-server-start.sh config/server-1.properties &
# JMX_PORT=9998 bin/kafka-server-start.sh config/server-2.properties &
����һ��topic
# bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 3 --partition 1 --topic my-replicated-topiclistһ��
# bin/kafka-list-topic.sh --zookeeper localhost:2181   
topic: my-replicated-topic    partition: 0    leader: 2    replicas: 2,0,1    isr: 2,0,1
topic: test    partition: 0    leader: 0    replicas: 0    isr: 0partition��ͬһ��topic�¿������ö��partition����topic�µ�message�洢����ͬ��partition�£�Ŀ����Ϊ����߲�����
leader�������partition�Ķ�д������ÿ��broker���п��ܳ�Ϊĳpartition��leader
replicas������������partition���Ǽ���broker���б��ݣ�����broker�Ƿ���
isr������replicas
 