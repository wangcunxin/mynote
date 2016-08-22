#生成初始化脚本
  echo "
 #!/bin/bash
 mysql -e \"UPDATE  mysql.user SET password=password('$MYSQL_PASSWD')  where user='root'\"  
 "  > "${GALAXY_SETUP_HOME}"/tmp/mysql.sh

 chmod +x "${GALAXY_SETUP_HOME}"/tmp/mysql.sh

for node in $MASTER_NODES
do
#发送脚本到目标服务器上。
  $ssh_pass -p $ROOTPASSWD scp  "${GALAXY_SETUP_HOME}"/tmp/mysql.sh root@$node:/tmp

#以skip-grant-tables模式启动mysql服务
  $ssh_pass -p $ROOTPASSWD ssh  root@$node   /etc/init.d/mysql restart --skip-grant-tables

#修改root密码
  $ssh_pass -p $ROOTPASSWD ssh  root@$node  "/tmp/mysql.sh"

#以一般模式重启mysql服务
  $ssh_pass -p $ROOTPASSWD ssh  root@$node   /etc/init.d/mysql restart

done
 
#创建数据库，创建用户，用户访问授权.
echo "use mysql ;
      create database  if not exists  $MYSQL_DBNAME ;
      grant all privileges on $MYSQL_DBNAME.* to $MYSQL_USER@'%' identified by '$MYSQL_PASSWD' ;
     " >"${GALAXY_SETUP_HOME}"/tmp/admin_user.sql

#发送SQL到目标服务器上
for node in $MASTER_NODES
do
  "${ssh_pass}" -p $ROOTPASSWD scp  "${GALAXY_SETUP_HOME}"/tmp/admin_user.sql  root@$node:/tmp

  #运行sql添加用户

  "${ssh_pass}" -p $ROOTPASSWD ssh root@$node "mysql --user=root --password='$MYSQL_PASSWD' done

#配置高可用方案
#生成MySQL配置文件
echo "[mysqld]
server-id=1
log-bin=mysql-bin
replicate-do-db=hive
binlog-format=ROW
" > "${GALAXY_SETUP_HOME}"/tmp/my.cnf
#配置双向同步
echo "STOP SLAVE;
CHANGE MASTER TO MASTER_HOST='$NAMENODE2',MASTER_USER='$MYSQL_USER', MASTER_PASSWORD='$MYSQL_PASSWD' ;
START SLAVE;
"
>  "${GALAXY_SETUP_HOME}"/tmp/change_master.sql
 
 $ssh_pass -p $ROOTPASSWD   scp  "${GALAXY_SETUP_HOME}"/tmp/my.cnf  root@$NAMENODE1:/etc/
 $ssh_pass -p $ROOTPASSWD   scp  "${GALAXY_SETUP_HOME}"/tmp/change_master.sql root@$NAMENODE1:/tmp/
 $ssh_pass -p $ROOTPASSWD ssh  root@$NAMENODE1   /etc/init.d/mysql restart
 $ssh_pass -p $ROOTPASSWD ssh  root@$NAMENODE1  "mysql --user=root --password='$MYSQL_PASSWD' < /tmp/change_master.sql"


echo "[mysqld]
server-id=2
log-bin=mysql-bin
replicate-do-db=hive
binlog-format=ROW
" > "${GALAXY_SETUP_HOME}"/tmp/my.cnf

echo "STOP SLAVE;
CHANGE MASTER TO MASTER_HOST='$NAMENODE1',MASTER_USER='$MYSQL_USER', MASTER_PASSWORD='$MYSQL_PASSWD' ;
START SLAVE;
" >  "${GALAXY_SETUP_HOME}"/tmp/change_master.sql

 echo $NAMENODE1
 echo $NAMENODE2

 $ssh_pass -p $ROOTPASSWD  scp  "${GALAXY_SETUP_HOME}"/tmp/my.cnf  root@$NAMENODE2:/etc/
 $ssh_pass -p $ROOTPASSWD  scp  "${GALAXY_SETUP_HOME}"/tmp/change_master.sql root@$NAMENODE2:/tmp/
 $ssh_pass -p $ROOTPASSWD ssh  root@$NAMENODE2   /etc/init.d/mysql restart
 $ssh_pass -p $ROOTPASSWD ssh  root@$NAMENODE2  "mysql --user=root --password='$MYSQL_PASSWD'