#!/bin/bash

#����zookeeper
for node in $ZOOKEEPER_LIST ; do
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node "hbase-daemon.sh start zookeeper  "
done

#����journalnode ����
for node in $QJOURNAL_LIST ; do
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node  " hadoop-daemon.sh start journalnode " ;
 done

#��ʽ����namenode��

"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1  " hdfs namenode -format " ;

#������namenode�ڵ�hdfs����
 
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 "hadoop-daemon.sh start namenode  "


#��ʽ������namenode�ڵ�
"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE2 "hdfs namenode -bootstrapStandby "

#���������ڵ��namenode����
"${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE2 "hadoop-daemon.sh start namenode  "

#����datanode

for node in $DATA_NODE_LIST ; do
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node "hadoop-daemon.sh start datanode   "
done

#��ʽ��zkfc
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 " hdfs zkfc -formatZK  "

#����ZKFC
for node in $NAME_NODE_LIST ; do
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$node " hadoop-daemon.sh start zkfc  "
 done
#Ȩ������
source "${GALAXY_SETUP_HOME}"/sbin/set-ACL.sh
 
#ֹͣgalaxy
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass -p  $PASSWD ssh $GALAXY_USER@$NAMENODE1 "$GALAXY_HOME/sbin/stop-galaxy.sh"
