#!/bin/bash
connect_root="${GALAXY_SETUP_HOME}/tools/sshpass/sshpass -p $ROOTPASSWD ssh root@$NAMENODE1"
connect_galaxy="${GALAXY_SETUP_HOME}/tools/sshpass/sshpass -p $ROOTPASSWD ssh $GALAXY_USER@$NAMENODE1"

#��ʼ��Ȩ�޿���
 $connect_root "setfacl -R -m other::rwx $GALAXY_HOME/tmp" ;

 $connect_root "setfacl -R -m default:other::rwx $GALAXY_HOME/tmp" ;

 $connect_root "setfacl -R -m other::rwx $GALAXY_HOME/works" ;

 $connect_root "setfacl -R -m default:other::rwx $GALAXY_HOME/works"   ;

#hdfsȨ�޿��Ƴ�ʼ��
#����/windĿ¼��

$connect_galaxy "hadoop fs -mkdir /wind" ;
#����/wind�Ŀ���Ȩ��
$connect_galaxy "hadoop fs -setfacl -m other::--- /wind" ;

#����/tmpĿ¼��
$connect_galaxy "hadoop fs -mkdir /tmp" ;

#����/tmp�Ŀ���Ȩ��
$connect_galaxy "hadoop fs -setfacl -m other::rwx /tmp" ;
$connect_galaxy "hadoop fs -setfacl -m default:other::rwx /tmp" ;
#����hive�ķ���Ȩ��
#����hive�ĸ�Ŀ¼
$connect_galaxy hadoop fs -mkdir -p  /user/hive
#/user����Ϊ�����û���Ȩ�޽���
$connect_galaxy hadoop fs -setfacl -m     other::--x /user
#/user/hive����Ϊ�����û���Ȩ�޽���
$connect_galaxy hadoop fs -setfacl -m     other::--x /user/hive