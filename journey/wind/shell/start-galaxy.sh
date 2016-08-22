#!/bin/bash
#集群部署的Linux用户，集群的每个节点必须配置这个用户
#而且密码必须一致。
GALAXY_USER=galaxy
#galaxy部署的目录。
GALAXY_HOME=/home/$GALAXY_USER/galaxy
#需要安装的集群配置
#Master node 节点。
MASTER_NODES="hd01 hd02"
#Slave节点
SLAVE_NODES="hd03"

EXTEND_NODES=""
#name service 服务名称。
NAMESERVICE=galaxy
#集群中jdk安装目录。
JAVA_HOME=/home/$GALAXY_USER/galaxy/jdk

NAMENODE1=`echo $MASTER_NODES | cut -d " "  -f 1`
NAMENODE2=`echo $MASTER_NODES | cut -d " "  -f 2`
MYSQL_IP=$NAMENODE1
MYSQL_USER=hive
MYSQL_PASSWD=Wind2013
MYSQL_DBNAME=hive
MYSQL_PORT=3306
NAME_NODE_LIST=$MASTER_NODES
DATA_NODE_LIST=$SLAVE_NODES
QJOURNAL_LIST="$MASTER_NODES $SLAVE_NODES "
ZOOKEEPER_LIST="$MASTER_NODES $SLAVE_NODES "
EXTEND_NODE_LIST="$EXTEND_NODES"

#start-galaxy.sh
#!/bin/bash
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/"; pwd`

source $bin/galaxy-env.sh 

ZOOKEEPER_LIST=$ZOOKEEPER_LIST
NAMENODE1=$NAMENODE1
NAMENODE2=$NAMENODE2

#start zookeeper
for node in $ZOOKEEPER_LIST
do
 ssh "$GALAXY_USER@$node"  zkServer.sh start
done

#start hdfs
start-dfs.sh

#start YARN
start-yarn.sh 
ssh $GALAXY_USER@$NAMENODE2 yarn-daemon.sh  start resourcemanager

#start history server
mr-jobhistory-daemon.sh  start historyserver

#start hbase
start-hbase.sh
ssh $GALAXY_USER@$NAMENODE2 hbase-daemon.sh  start master