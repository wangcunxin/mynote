#!/bin/bash
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/.."; pwd`
GALAXY_SETUP_HOME=$bin
 if [ "`whoami`" = "root"  ] ; then
  chmod u+x ${GALAXY_SETUP_HOME}/tools/jdk/jdk-6u13-linux-x64.bin
  cd ${GALAXY_SETUP_HOME}/tools/jdk/ && ./jdk-6u13-linux-x64.bin && mv jdk1.6.0_13 ${GALAXY_SETUP_HOME}/galaxy/jdk
  else
    echo "使用root权限执行该脚本" && exit 1 ;
fi
tar -xzvf ${GALAXY_SETUP_HOME}/tools/zookeeper/zookeeper-3.4.5.tar.gz && mv zookeeper-3.4.5 ${GALAXY_SETUP_HOME}/galaxy/zk
tar -xzvf ${GALAXY_SETUP_HOME}/tools/storm/apache-storm-0.9.1-incubating.tar.gz && mv apache-storm-0.9.1-incubating ${GALAXY_SETUP_HOME}/galaxy/storm