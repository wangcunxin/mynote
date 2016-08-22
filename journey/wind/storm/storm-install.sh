#!/bin/sh
#0.一键式安装storm集群
#1.设置免密码登录
#2.解压、改名(安装包：jdk、zookeeper、storm)
#3.复制配置文件：hosts、profile、zoo.cfg、storm.yaml
#4.复制zookeeper、storm、jdk到其它所有节点上
bin=`dirname "${BASH_SOURCE-$0}"`
bin=`cd "$bin/.."; pwd`
function usage(){
 echo " install.sh < -r|-p > "
 echo " -r 执行清洁安装，删除已有的安装文件。"
 echo " -p 不删除已有的安装文件，覆盖安装。"  
}
#参数个数不等于1
if [ ! $# -eq 1 ] ; then
  usage
  exit 1 ;
fi
#substr截取参数(1,1)
if [ `expr substr $1 1 1` = - ]  ; then
 if [  $1 = "-r" ] ; then
    echo "执行清洁安装，删除已有的安装文件。"
    flag=$1 
 elif [ $1 = "-p" ] ; then
    echo "不删除已有安装文件，覆盖安装。"
    flag=$1
    else
     usage
     exit 1
 fi 
fi
GALAXY_SETUP_HOME=$bin
GALAXY_CONF_DIR=$GALAXY_SETUP_HOME/conf
GALAXY_CONF_DIR=`cd "$GALAXY_CONF_DIR" ; pwd`
#默认galaxy用户
GALAXY_USER=bihadoop
#默认galaxy安装目录
GALAXY_HOME=/home/$GALAXY_USER/galaxy
#使用用户配置的环境变量
source "$GALAXY_CONF_DIR"/galaxy-env.sh
#?处理密码
getchar() {
     stty cbreak -echo
     dd if=/dev/tty bs=1 count=1 2>/dev/null
     stty -cbreak echo
 }
echo "请输入集群中每个节点的$GALAXY_USER用户的密码"
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
echo "请输入集群中每个节点的root的密码"
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
echo  "安装的用户：$GALAXY_USER"
echo  "安装目录：$GALAXY_HOME"
#2.解压缩安装包文件
 if ! [ -d ${GALAXY_SETUP_HOME}/galaxy/storm  ] ; then
   mkdir -p ${GALAXY_SETUP_HOME}/galaxy
   source ${GALAXY_SETUP_HOME}/sbin/uncompress.sh
 fi
#3.复制配置文件：hosts、profile、zoo.cfg、storm.yaml
#修改客户端系统的hosts，用主机名代替IP地址方便用户维护，需要root用户密码。
 if [ "`whoami`" = "root"  ] ; then
    cat "${GALAXY_CONF_DIR}"/hosts > /etc/hosts
  else
    echo "使用root权限执行该脚本" && exit 1 ;
fi
#将hosts添加到集群中的其他节点的hosts中。
for node in  $SLAVE_NODES  ; do
  $GALAXY_SETUP_HOME/tools/sshpass/sshpass/bin/sshpass -p $ROOTPASSWD  ssh -o StrictHostKeyChecking=no   root@$node  "cat > /etc/hosts;"  < "${GALAXY_CONF_DIR}"/hosts
done
#1.在node上生成公私密钥对，并拷贝到本地
for node in $MASTER_NODES $SLAVE_NODES  ; do
  #生成公私密钥
  "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$node  "rm -f /home/$GALAXY_USER/.ssh/id_rsa &&ssh-keygen  -t rsa -P '' -f /home/$GALAXY_USER/.ssh/id_rsa "
  #修改ssh配置文件,连接时不用确认
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh  $GALAXY_USER@$node  "echo 'StrictHostKeyChecking no
UserKnownHostsFile /dev/null
' > /home/$GALAXY_USER/.ssh/config "
  #拷贝公钥到本地
 "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$node   "cat ~/.ssh/id_rsa.pub  " | cat > $GALAXY_SETUP_HOME/tmp/"$node"_rsa.pub
  #将每个node上的公钥加到其他其他节点上。
  for slave in $MASTER_NODES $SLAVE_NODES ; do
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass  -p $PASSWD  ssh -o StrictHostKeyChecking=no $GALAXY_USER@$slave "mkdir -p  /home/${GALAXY_USER}/.ssh && cat >> /home/${GALAXY_USER}/.ssh/authorized_keys " < $GALAXY_SETUP_HOME/tmp/"$node"_rsa.pub  2>&1  | sed   "s/^/$slave: /"
  done
done
#添加系统运行需要的环境变量
GALAXY_WORKS=$GALAXY_HOME/works
 >  "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export JAVA_HOME=$JAVA_HOME" >>"$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export GALAXY_HOME=$GALAXY_HOME" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export ZK_HOME=$ZK_HOME" >>"$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export STORM_HOME=$STORM_HOME" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"
echo  "export PATH=\$GALAXY_HOME/sbin:\$JAVA_HOME/bin:\$ZK_HOME/bin:\$STORM_HOME/bin:\$PATH" >> "$GALAXY_SETUP_HOME/tmp/.bashrc"

#修改配置文件zk、storm
rm ${GALAXY_SETUP_HOME}/galaxy/zk/conf/zoo_sample.cfg
cp ${GALAXY_CONF_DIR}/zoo.cfg ${GALAXY_SETUP_HOME}/galaxy/zk/conf/
mkdir -p "/tmp/zookeeper/data" && cd /tmp/zookeeper/data && touch myid && echo "1" > myid
cp ${GALAXY_CONF_DIR}/storm.yaml ${GALAXY_SETUP_HOME}/galaxy/storm/conf/
cd ${GALAXY_SETUP_HOME}/galaxy/storm/lib/ && rm -rf log4j-over-slf4j-1.6.6.jar
cp ${GALAXY_SETUP_HOME}/galaxy/zk/lib/log4j-1.2.15.jar .
#将galaxy的配置文件拷贝到运行包中，在运行包启动或者关闭的时候会用到这些配置文件。
 cp "$GALAXY_SETUP_HOME"/conf/galaxy-env.sh  "$GALAXY_SETUP_HOME/sbin/"  ;
 
echo "安装程序分发中..."
#将配置好的安装文件分发到集群上。
for node in $MASTER_NODES $SLAVE_NODES ;
 do
  echo "安装程序向$node节点分发中..."
  if [ $flag = "-r" ] ; then
   #彻底删除原来的目录
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh  $GALAXY_USER@$node "rm -rf $GALAXY_HOME" ;   
  fi
   #确保目录已经创建
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh   $GALAXY_USER@$node  "mkdir -p $GALAXY_HOME" ;
   #分发数据
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD scp -r  $GALAXY_SETUP_HOME/galaxy/*   $GALAXY_USER@$node:$GALAXY_HOME/ ; 
 
   #将配置完成的环境变量发送到集群中每个节点上。
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p $PASSWD ssh  $GALAXY_USER@$node " cat > /home/$GALAXY_USER/.bashrc " < $GALAXY_SETUP_HOME/tmp/.bashrc ;   
done
#确保Jdk已经安装，java可以正常使用
for node in  $MASTER_NODES $SLAVE_NODES ; do
   "${GALAXY_SETUP_HOME}"/tools/sshpass/sshpass/bin/sshpass -p  $PASSWD ssh $GALAXY_USER@$node  "which java "  2>&1  | sed       "s/^/$node: /"
done
 