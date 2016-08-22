#!/bin/bash

#启动zookeeper
for node in $ZOOKEEPER_LIST ; do
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node "hbase-daemon.sh start zookeeper  "
done

#启动journalnode 服务。
for node in $QJOURNAL_LIST ; do
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node  " hadoop-daemon.sh start journalnode " ;
 done

#格式化主namenode。

"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1  " hdfs namenode -format " ;

#启动主namenode节点hdfs服务。
 
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 "hadoop-daemon.sh start namenode  "


#格式化辅助namenode节点
"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE2 "hdfs namenode -bootstrapStandby "

#启动辅助节点的namenode服务
"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE2 "hadoop-daemon.sh start namenode  "

#启动datanode

for node in $DATA_NODE_LIST ; do
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node "hadoop-daemon.sh start datanode   "
done

#格式化zkfc
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 " hdfs zkfc -formatZK  "

#启动ZKFC
for node in $NAME_NODE_LIST ; do
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node " hadoop-daemon.sh start zkfc  "
 done
#权限设置
source "${GALAXY_SETUP_HOME}"/sbin/set-ACL.sh
 
#停止galaxy
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 "$GALAXY_HOME/sbin/stop-galaxy.sh"
