#���ɳ�ʼ���ű�
  echo "
 #!/bin/bash
 mysql -e \"UPDATE  mysql.user SET password=password('$MYSQL_PASSWD')  where user='root'\"  
 "  > "${GALAXY_SETUP_HOME}"/tmp/mysql.sh

 chmod +x "${GALAXY_SETUP_HOME}"/tmp/mysql.sh

for node in $MASTER_NODES
do
#���ͽű���Ŀ��������ϡ�
  $ssh_pass -p $ROOTPASSWD scp  "${GALAXY_SETUP_HOME}"/tmp/mysql.sh root@$node:/tmp

#��skip-grant-tablesģʽ����mysql����
  $ssh_pass -p $ROOTPASSWD ssh  root@$node   /etc/init.d/mysql restart --skip-grant-tables

#�޸�root����
  $ssh_pass -p $ROOTPASSWD ssh  root@$node  "/tmp/mysql.sh"

#��һ��ģʽ����mysql����
  $ssh_pass -p $ROOTPASSWD ssh  root@$node   /etc/init.d/mysql restart

done
 
#�������ݿ⣬�����û����û�������Ȩ.
echo "use mysql ;
      create database  if not exists  $MYSQL_DBNAME ;
      grant all privileges on $MYSQL_DBNAME.* to $MYSQL_USER@'%' identified by '$MYSQL_PASSWD' ;
     " >"${GALAXY_SETUP_HOME}"/tmp/admin_user.sql

#����SQL��Ŀ���������
for node in $MASTER_NODES
do
  "${ssh_pass}" -p $ROOTPASSWD scp  "${GALAXY_SETUP_HOME}"/tmp/admin_user.sql  root@$node:/tmp

  #����sql����û�

  "${ssh_pass}" -p $ROOTPASSWD ssh root@$node "mysql --user=root --password='$MYSQL_PASSWD' done

#���ø߿��÷���
#����MySQL�����ļ�
echo "[mysqld]
server-id=1
log-bin=mysql-bin
replicate-do-db=hive
binlog-format=ROW
" > "${GALAXY_SETUP_HOME}"/tmp/my.cnf
#����˫��ͬ��
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