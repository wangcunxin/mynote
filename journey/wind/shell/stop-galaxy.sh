#!/bin/bash

bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/"; pwd`

source $bin/galaxy-env.sh 

ZOOKEEPER_LIST=$ZOOKEEPER_LIST
NAMENODE1=$NAMENODE1
NAMENODE2=$NAMENODE2

#stop history server
 mr-jobhistory-daemon.sh  stop historyserver

#stop hbase 
stop-hbase.sh
ssh "$GALAXY_USER@$NAMENODE2"  hbase-daemon.sh stop master

#stop YARN
stop-yarn.sh 
ssh $GALAXY_USER@$NAMENODE2 yarn-daemon.sh  stop resourcemanager

#stop hdfs
stop-dfs.sh

#stop zookeeper
for node in $ZOOKEEPER_LIST
do
 ssh "$GALAXY_USER@$node"  zkServer.sh stop zookeeper
done