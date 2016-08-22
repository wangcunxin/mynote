#!/bin/bash
#集群部署的Linux用户,集群的每个节点必须配置这个用户,而且密码必须一致。
GALAXY_USER=bihadoop
#galaxy部署的目录。
GALAXY_HOME=/home/$GALAXY_USER/galaxy
#需要安装的集群配置
#Master node 节点。
MASTER_NODES="storm001"
#Slave节点
SLAVE_NODES="storm002 storm003"
#Zookeeper集群节点
ZK_NODES="storm002"