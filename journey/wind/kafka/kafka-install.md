1.从http://kafka.apache.org/下载kafka安装包；
2.tar zxvf kafka_2.8.0.tar.gz,修改配置文件conf/server.properties：
broker.id=0；host.name=10.100.5.9；zookeeper.connect=10.100.5.9:2181可逗号分隔配置多个
3.启动服务：bin/zkServer.sh start； bin/kafka-server-start.sh config/server.properties &
4.创建topic主题：
bin/kafka-create-topic.sh --zookeeper 10.100.5.9:2181 --replica 3 --partition 1 --topic mykafka
//启动报错Unrecognized VM option '+UseCompressedOops'
查看 bin/kafka-run-class.sh
找到
if [ -z "$KAFKA_JVM_PERFORMANCE_OPTS" ]; then
  KAFKA_JVM_PERFORMANCE_OPTS="-server  -XX:+UseCompressedOops -XX:+UseParNewGC -XX:+UseConcMarkSweepGC -XX:+CMSClassUnloadingEnabled -XX:+CMSScavengeBeforeRemark -XX:+DisableExplicitGC -Djava.awt.headless=true"
fi
去掉-XX:+UseCompressedOops
5.测试主题：
bin/kafka-list-topic.sh --zookeeper 10.100.5.9:2181
6.生产者发送消息：
bin/kafka-console-producer.sh --broker-list 10.100.5.9:9092 --topic mykafka
7.消费者接收消息：
bin/kafka-console-consumer.sh --zookeeper 10.100.5.9:2181 --topic mykafka --from-beginning
8.多broker测试
之前是一台机器上当做一个node，现在尝试在一台机器上放3个node，即broker
修改配置文件
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
启动broker1和broker2
# JMX_PORT=9997 bin/kafka-server-start.sh config/server-1.properties &
# JMX_PORT=9998 bin/kafka-server-start.sh config/server-2.properties &
创建一个topic
# bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 3 --partition 1 --topic my-replicated-topiclist一下
# bin/kafka-list-topic.sh --zookeeper localhost:2181   
topic: my-replicated-topic    partition: 0    leader: 2    replicas: 2,0,1    isr: 2,0,1
topic: test    partition: 0    leader: 0    replicas: 0    isr: 0partition：同一个topic下可以设置多个partition，将topic下的message存储到不同的partition下，目的是为了提高并行性
leader：负责此partition的读写操作，每个broker都有可能成为某partition的leader
replicas：副本，即此partition在那几个broker上有备份，不管broker是否存活
isr：存活的replicas
 