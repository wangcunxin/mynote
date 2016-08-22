#!/bin/sh
#0.һ��ʽ��װstorm��Ⱥ
#1.�����������¼
#2.��ѹ������(��װ����jdk��zookeeper��storm)
#3.���������ļ���hosts��profile��zoo.cfg��storm.yaml
#4.����zookeeper��storm��jdk���������нڵ���
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/.."; pwd`
function usage(){
 echo " install.sh < -r|-p > "
 echo " -r ִ����లװ��ɾ�����еİ�װ�ļ���"
 echo " -p ��ɾ�����еİ�װ�ļ������ǰ�װ��"  
}
#��������������1
if [ ! $# -eq 1 ] ; then
  usage
  exit 1 ;
fi
#substr��ȡ����(1,1)
if [ `expr substr $1 1 1` = - ]  ; then
 if [  $1 = "-r" ] ; then
    echo "ִ����లװ��ɾ�����еİ�װ�ļ���"
    flag=$1 
 elif [ $1 = "-p" ] ; then
    echo "��ɾ�����а�װ�ļ������ǰ�װ��"
    flag=$1
    else
     usage
     exit 1
 fi 
fi
GALAXY_SETUP_HOME=$bin
GALAXY_CONF_DIR=$GALAXY_SETUP_HOME/conf
GALAXY_CONF_DIR=`cd "$GALAXY_CONF_DIR" ; pwd`
#Ĭ��galaxy�û�
GALAXY_USER=bihadoop
#Ĭ��galaxy��װĿ¼
GALAXY_HOME=/home/$GALAXY_USER/galaxy
#ʹ���û����õĻ�������
source "$GALAXY_CONF_DIR"/galaxy-env.sh
#?��������
getchar() {
     stty cbreak -echo
     dd if=/dev/tty bs=1 count=1 2>/dev/null
     stty -cbreak echo
 }
echo "�����뼯Ⱥ��ÿ���ڵ��$GALAXY_USER�û�������"
while : ; do
     ret=`getchar`
     if [ "$ret" =  "" ]; then
         echo
         break
     fi 
     str="$str$ret"
    printf "*"
done
PASSWD=$str ;
str=""
echo "�����뼯Ⱥ��ÿ���ڵ��root������"
while : ; do
     ret=`getchar`
     if [ "$ret" =  "" ]; then
         echo
         break
     fi 
     str="$str$ret"
    printf "*"
done
ROOTPASSWD=$str

JAVA_HOME="$GALAXY_HOME"/jdk
ZK_HOME="$GALAXY_HOME"/zk
STORM_HOME="$GALAXY_HOME"/storm
echo  "��װ���û���$GALAXY_USER"
echo  "��װĿ¼��$GALAXY_HOME"
#2.��ѹ����װ���ļ�
 if ! [ -d ${GALAXY_SETUP_HOME}/galaxy/storm  ] ; then
   mkdir -p ${GALAXY_SETUP_HOME}/galaxy
   source ${GALAXY_SETUP_HOME}/sbin/uncompress.sh
 fi
#3.���������ļ���hosts��profile��zoo.cfg��storm.yaml
#�޸Ŀͻ���ϵͳ��hosts��������������IP��ַ�����û�ά������Ҫroot�û����롣
 if [ "`whoami`" = "root"  ] ; then
    cat "${GALAXY_CONF_DIR}"/hosts > /etc/hosts
  else
    echo "ʹ��rootȨ��ִ�иýű�" && exit 1 ;
fi
#��hosts��ӵ���Ⱥ�е������ڵ��hosts�С�
for node in  $SLAVE_NODES  ; do
  $GALAXY_SETUP_HOME/tools/sshpass/sshpass/bin/sshpass -p $ROOTPASSWD  ssh -o StrictHostKeyChecking=no   root@$node  "cat > /etc/hosts;"  < "${GALAXY_CONF_DIR}"/hosts
done
#1.��node�����ɹ�˽��Կ�ԣ�������������
for node in $MASTER_NODES $SLAVE_NODES  ; do
  #���ɹ�˽��Կ
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$node  "rm -f /home/$GALAXY_USER/.ssh/id_rsa &&ssh-keygen  -t rsa -P '' -f /home/$GALAXY_USER/.ssh/id_rsa "
  #�޸�ssh�����ļ�,����ʱ����ȷ��
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh  $GALAXY_USER@$node  "echo 'StrictHostKeyChecking no
UserKnownHostsFile /dev/null
' > /home/$GALAXY_USER/.ssh/config "
  #������Կ������
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$node   "cat ~/.ssh/id_rsa.pub  " | cat > $GALAXY_SETUP_HOME/tmp/"$node"_rsa.pub
  #��ÿ��node�ϵĹ�Կ�ӵ����������ڵ��ϡ�
  for slave in $MASTER_NODES $SLAVE_NODES ; do
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$slave "mkdir -p  /home/${GALAXY_USER}/.ssh && cat >> /home/${GALAXY_USER}/.ssh/authorized_keys " < $GALAXY_SETUP_HOME/tmp/"$node"_rsa.pub  2>&1  | sed   "s/^/$slave: /"
  done
done
#���ϵͳ������Ҫ�Ļ�������
GALAXY_WORKS=$GALAXY_HOME/works
 >  "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export JAVA_HOME=$JAVA_HOME" >>"$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export GALAXY_HOME=$GALAXY_HOME" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export ZK_HOME=$ZK_HOME" >>"$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export STORM_HOME=$STORM_HOME" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export PATH=\$GALAXY_HOME/sbin:\$JAVA_HOME/bin:\$ZK_HOME/bin:\$STORM_HOME/bin:\$PATH" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"

#�޸������ļ�zk��storm
rm ${GALAXY_SETUP_HOME}/galaxy/zk/conf/zoo_sample.cfg
cp ${GALAXY_CONF_DIR}/zoo.cfg ${GALAXY_SETUP_HOME}/galaxy/zk/conf/
mkdir -p "/tmp/zookeeper/data" && cd /tmp/zookeeper/data && touch myid && echo "1" > myid
cp ${GALAXY_CONF_DIR}/storm.yaml ${GALAXY_SETUP_HOME}/galaxy/storm/conf/
cd ${GALAXY_SETUP_HOME}/galaxy/storm/lib/ && rm -rf log4j-over-slf4j-1.6.6.jar
cp ${GALAXY_SETUP_HOME}/galaxy/zk/lib/log4j-1.2.15.jar .
#��galaxy�������ļ����������а��У������а��������߹رյ�ʱ����õ���Щ�����ļ���
 cp "$GALAXY_SETUP_HOME"/conf/galaxy-env.sh  "$GALAXY_SETUP_HOME/sbin/"  ;
 
echo "��װ����ַ���..."
#�����úõİ�װ�ļ��ַ�����Ⱥ�ϡ�
for node in $MASTER_NODES $SLAVE_NODES ;
 do
  echo "��װ������$node�ڵ�ַ���..."
  if [ $flag = "-r" ] ; then
   #����ɾ��ԭ����Ŀ¼
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh  $GALAXY_USER@$node "rm -rf $GALAXY_HOME" ;   
  fi
   #ȷ��Ŀ¼�Ѿ�����
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh   $GALAXY_USER@$node  "mkdir -p $GALAXY_HOME" ;
   #�ַ�����
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD scp -r  $GALAXY_SETUP_HOME/galaxy/*   $GALAXY_USER@$node:$GALAXY_HOME/ ; 
 
   #��������ɵĻ����������͵���Ⱥ��ÿ���ڵ��ϡ�
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh  $GALAXY_USER@$node " cat > /home/$GALAXY_USER/.bashrc " < $GALAXY_SETUP_HOME/tmp/.bashrc ;   
done
#ȷ��Jdk�Ѿ���װ��java��������ʹ��
for node in  $MASTER_NODES $SLAVE_NODES ; do
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p  $PASSWD ssh $GALAXY_USER@$node  "which java "  2>&1  | sed       "s/^/$node: /"
done
 