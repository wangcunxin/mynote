一、通过flume将日志发送到hdfs
1.配置hadoop环境(略)
    10.2.2.104 是Master，运行NameNode
    10.2.2.113 是Slave1，运行DataNode
    10.2.2.114 是Slave2，运行DataNode
 
    配置好之后，通过主机的start-all.sh启动集群
 
2.安装flume
    下载 wget http://mirror.bit.edu.cn/apache/flume/1.6.0/apache-flume-1.6.0-bin.tar.gz
    解压 tar -zxvf apache-flume-1.6.0-bin.tar.gz
 
3.修改flume配置文件
    进入conf目录,将flume-conf.properties.template,flume-env.sh.template这两个示例文件复制一份
    cp flume-conf.properties.template flume-conf.properties
    cp flume-env.sh.template flume-env.sh
    ...
    新建my-flume.properties
# 定义传输方式(通过内存)
agent1.channels.ch1.type = memory
agent1.channels.ch1.capacity = 100000
agent1.channels.ch1.transactionCapacity = 100000
agent1.channels.ch1.keep-alive = 30

# 定义传输方式(通过文件)
#agent1.channels.ch1.type = file
#agent1.channels.ch1.dataDirs = /usr/local/flume-1.6.0/file-channel/data
#agent1.channels.ch1.checkpointDir = /usr/local/flume-1.6.0/file-channel/checkpoint
#agent1.channels.ch1.capacity = 200000000
#agent1.channels.ch1.transactionCapacity = 100000
#agent1.channels.ch1.checkpointInterval = 30000
 
# 定义来源(读取通过avro发送到指定端口的消息)
agent1.sources.avro-source1.channels = ch1
agent1.sources.avro-source1.type = avro
agent1.sources.avro-source1.bind = 0.0.0.0
agent1.sources.avro-source1.port = 41414
agent1.sources.avro-source1.threads = 5
 
# 定义来源(执行shell命令,读取日志文件)
#agent1.sources.avro-source1.type = exec
#agent1.sources.avro-source1.shell = /bin/bash -c
#agent1.sources.avro-source1.command = cat /opt/apache-tomcat-hippo/logs/catalina.2015-12-18.log
#agent1.sources.avro-source1.channels = ch1
#agent1.sources.avro-source1.threads = 5

# 定义来源(监控指定目录文件变化)
#agent1.sources.avro-source1.type = spooldir
#agent1.sources.avro-source1.channels = ch1
#agent1.sources.avro-source1.spoolDir = /var/log/flumeTest
#agent1.sources.avro-source1.fileHeader = true
 
# 定义输出目标(输出到HDFS)
agent1.sinks.log-sink1.channel = ch1
agent1.sinks.log-sink1.type = hdfs
agent1.sinks.log-sink1.hdfs.path = hdfs://10.2.2.104:9000/apilog/%Y%m/%d/%H
agent1.sinks.log-sink1.hdfs.filePrefix = apilog
agent1.sinks.log-sink1.hdfs.writeFormat = Text
agent1.sinks.log-sink1.hdfs.fileType = DataStream
agent1.sinks.log-sink1.hdfs.rollInterval = 0
agent1.sinks.log-sink1.hdfs.rollSize = 128000000
agent1.sinks.log-sink1.hdfs.rollCount = 0
agent1.sinks.log-sink1.hdfs.batchSize = 10000
agent1.sinks.log-sink1.hdfs.txnEventMax = 1000
agent1.sinks.log-sink1.hdfs.callTimeout = 60000
agent1.sinks.log-sink1.hdfs.appendTimeout = 60000
agent1.sinks.log-sink1.hdfs.minBlockReplicas = 1
agent1.sinks.log-sink1.hdfs.useLocalTimeStamp = true
agent1.sinks.log-sink1.serializer.appendNewline = false
 
# 配置channels,sources,sinks
agent1.channels = ch1
agent1.sources = avro-source1
agent1.sinks = log-sink1

4.添加连接hdfs需要的jar包
将以下Jar包复制到flume的lib目录,需要对应hadoop版本
commons-configuration
hadoop-auth
hadoop-client
hadoop-common
hadoop-hdfs
htrace-core

5.执行启动命令
/usr/local/flume-1.6.0/bin/flume-ng agent --conf /usr/local/flume-1.6.0/conf --conf-file /usr/local/flume-1.6.0/conf/my-flume.conf --name agent1 -Dflume.root.logger=INFO,console
 
6.使用hdfs命令查看
在hadoop集群中,使用命令查看hdfs上的文件
hadoop fs -ls /flumeTest


二、通过flume收集多个项目的日志文件
主要功能：收集 Java ， PHP 服务端后台日志和 Nginx 访问日志，分类型保存到 HDFS 中
app -->agent1-->collector1-->hdfs
h5-->agent2
nginx-->agent3-->collector2
php-->agent4


flume-agent配置说明
agent.sources = tailsource-1 tailsource-2
agent.channels = memoryChannel-1
agent.sinks = remotesink-1 remotesink-2
 
# 设置多个source，通过执行shell命令读取不同的日志
agent.sources.tailsource-1.type = exec
agent.sources.tailsource-1.command = tail -F /opt/apache-tomcat-client-service/logs/catalina.out
agent.sources.tailsource-1.channels = memoryChannel-1
 
agent.sources.tailsource-2.type = exec
agent.sources.tailsource-2.command = tail -F /var/log/nginx/access.log
agent.sources.tailsource-2.channels = memoryChannel-1
 
# 给每个source指定interceptor，目的给日志指定header，区分不同来源和类型
agent.sources.tailsource-1.interceptors=inter1
agent.sources.tailsource-1.interceptors.inter1.type = static
agent.sources.tailsource-1.interceptors.inter1.key = log_type
agent.sources.tailsource-1.interceptors.inter1.value = client-service-all
 
agent.sources.tailsource-2.interceptors=inter2
agent.sources.tailsource-2.interceptors.inter2.type = static
agent.sources.tailsource-2.interceptors.inter2.key = log_type
agent.sources.tailsource-2.interceptors.inter2.value = client-service-stat
 
# 设置channel，使用了内存通道，多个source可以共享
agent.channels.memoryChannel-1.type = memory
agent.channels.memoryChannel-1.keep-alive = 10
agent.channels.memoryChannel-1.capacity = 100000
agent.channels.memoryChannel-1.transactionCapacity =100000
 
# 设置多个sink，用avro协议发送到多个日志收集服务器
agent.sinks.remotesink-1.type= avro
agent.sinks.remotesink-1.channel= memoryChannel-1
agent.sinks.remotesink-1.hostname= 192.168.0.135
agent.sinks.remotesink-1.port= 41414
 
agent.sinks.remotesink-2.type= avro
agent.sinks.remotesink-2.channel= memoryChannel-1
agent.sinks.remotesink-2.hostname= 10.2.2.114
agent.sinks.remotesink-2.port= 41414
 
# 把多个sink加入到组，配置成负载均衡模式
agent.sinkgroups=g1
agent.sinkgroups.g1.sinks=remotesink-1 remotesink-2
agent.sinkgroups.g1.processor.type=load_balance
agent.sinkgroups.g1.processor.backoff=true
agent.sinkgroups.g1.processor.selector=random
1.设置多个 source ，通过执行 shell 命令读取不同的日志
2.给每个 source 指定 interceptor ，目的给日志指定 header ，区分不同来源和类型
3.设置 channel ，使用了内存通道，多个 source 可以共享
4.设置多个 sink ，用 avro 协议发送到多个日志收集服务器
5.把多个 sink 加入到组，配置成负载均衡模式
 
进行以上配置之后，flume-agent 可以
1.监听 client-service 和 nginx 最新的日志，并给日志标记类型
2.把日志通过内存通道发送到 (192.168.0.135,10.2.2.114) 两台服务器中的一台
 
flume-collector配置说明
agent1.channels = memory-statlog-channel memory-alllog-channel
agent1.sources = avro-source
agent1.sinks = hdfs-statlog-sink hdfs-alllog-sink
# 设置通过avro接收消息的端口和处理通道，可配置多个channel
agent1.sources.avro-source.channels = memory-statlog-channel memory-alllog-channel
agent1.sources.avro-source.type = avro
agent1.sources.avro-source.bind = 0.0.0.0
agent1.sources.avro-source.port = 41414
agent1.sources.avro-source.threads = 5
# 设置多路复用的分配规则，按照header中的字段值区分
agent1.sources.avro-source.selector.type = multiplexing
agent1.sources.avro-source.selector.header = log_type
agent1.sources.avro-source.selector.mapping.client-service-stat = memory-statlog-channel
agent1.sources.avro-source.selector.mapping.client-service-all = memory-alllog-channel
# 定义传输方式(通过内存)
agent1.channels.memory-statlog-channel.type = memory
agent1.channels.memory-statlog-channel.capacity = 100000
agent1.channels.memory-statlog-channel.transactionCapacity = 100000
agent1.channels.memory-statlog-channel.keep-alive = 30
agent1.channels.memory-alllog-channel.type = memory
agent1.channels.memory-alllog-channel.capacity = 100000
agent1.channels.memory-alllog-channel.transactionCapacity = 100000
agent1.channels.memory-alllog-channel.keep-alive = 30
# 定义输出目标(输出到HDFS)
agent1.sinks.hdfs-statlog-sink.channel = memory-statlog-channel
agent1.sinks.hdfs-statlog-sink.type = hdfs
agent1.sinks.hdfs-statlog-sink.hdfs.path = hdfs://10.2.2.104:9000/apilog/%Y%m/%d/%H
agent1.sinks.hdfs-statlog-sink.hdfs.filePrefix = apilog
agent1.sinks.hdfs-statlog-sink.hdfs.writeFormat = Text
agent1.sinks.hdfs-statlog-sink.hdfs.fileType = DataStream
agent1.sinks.hdfs-statlog-sink.hdfs.rollInterval = 0
agent1.sinks.hdfs-statlog-sink.hdfs.rollSize = 128000000
agent1.sinks.hdfs-statlog-sink.hdfs.rollCount = 0
agent1.sinks.hdfs-statlog-sink.hdfs.batchSize = 10000
agent1.sinks.hdfs-statlog-sink.hdfs.txnEventMax = 1000
agent1.sinks.hdfs-statlog-sink.hdfs.callTimeout = 60000
agent1.sinks.hdfs-statlog-sink.hdfs.appendTimeout = 60000
agent1.sinks.hdfs-statlog-sink.hdfs.minBlockReplicas = 1
agent1.sinks.hdfs-statlog-sink.hdfs.useLocalTimeStamp = true
agent1.sinks.hdfs-statlog-sink.serializer.appendNewline = false
agent1.sinks.hdfs-alllog-sink.channel = memory-alllog-channel
agent1.sinks.hdfs-alllog-sink.type = hdfs
agent1.sinks.hdfs-alllog-sink.hdfs.path = hdfs://10.2.2.104:9000/data/client-service/%Y%m%d
agent1.sinks.hdfs-alllog-sink.hdfs.filePrefix = apilog
agent1.sinks.hdfs-alllog-sink.hdfs.writeFormat = Text
agent1.sinks.hdfs-alllog-sink.hdfs.fileType = DataStream
agent1.sinks.hdfs-alllog-sink.hdfs.rollInterval = 0
agent1.sinks.hdfs-alllog-sink.hdfs.rollSize = 128000000
agent1.sinks.hdfs-alllog-sink.hdfs.rollCount = 0
agent1.sinks.hdfs-alllog-sink.hdfs.batchSize = 10000
agent1.sinks.hdfs-alllog-sink.hdfs.txnEventMax = 1000
agent1.sinks.hdfs-alllog-sink.hdfs.callTimeout = 60000
agent1.sinks.hdfs-alllog-sink.hdfs.appendTimeout = 60000
agent1.sinks.hdfs-alllog-sink.hdfs.minBlockReplicas = 1
agent1.sinks.hdfs-alllog-sink.hdfs.useLocalTimeStamp = true
agent1.sinks.hdfs-alllog-sink.serializer.appendNewline = true
1.设置通过 avro 接收消息的端口和处理通道，可配置多个 channel
2.设置多路复用的分配规则，按照 header 中的字段值区分
3.定义传输方式(通过内存)
4.定义输出目标(输出到HDFS)

进行以上配置之后，flume-collector可以
1.接收所有建立连接的 agent 发送来的日志
2.按照日志 header 中的 log_type 把日志分到不同的传输通道
3.把不同类型的日志保存在HDFS上的不同目录