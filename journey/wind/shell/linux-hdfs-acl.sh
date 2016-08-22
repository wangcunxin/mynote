#!/bin/bash
connect_root="${GALAXY_SETUP_HOME}/tools/sshpass/sshpass -p $ROOTPASSWD ssh root@$NAMENODE1"
connect_galaxy="${GALAXY_SETUP_HOME}/tools/sshpass/sshpass -p $ROOTPASSWD ssh $GALAXY_USER@$NAMENODE1"

#初始化权限控制
 $connect_root "setfacl -R -m other::rwx $GALAXY_HOME/tmp" ;

 $connect_root "setfacl -R -m default:other::rwx $GALAXY_HOME/tmp" ;

 $connect_root "setfacl -R -m other::rwx $GALAXY_HOME/works" ;

 $connect_root "setfacl -R -m default:other::rwx $GALAXY_HOME/works"   ;

#hdfs权限控制初始化
#创建/wind目录。

$connect_galaxy "hadoop fs -mkdir /wind" ;
#设置/wind的控制权限
$connect_galaxy "hadoop fs -setfacl -m other::--- /wind" ;

#创建/tmp目录。
$connect_galaxy "hadoop fs -mkdir /tmp" ;

#设置/tmp的控制权限
$connect_galaxy "hadoop fs -setfacl -m other::rwx /tmp" ;
$connect_galaxy "hadoop fs -setfacl -m default:other::rwx /tmp" ;
#设置hive的访问权限
#创建hive的根目录
$connect_galaxy hadoop fs -mkdir -p  /user/hive
#/user设置为所有用户有权限进入
$connect_galaxy hadoop fs -setfacl -m     other::--x /user
#/user/hive设置为所有用户有权限进入
$connect_galaxy hadoop fs -setfacl -m     other::--x /user/hive