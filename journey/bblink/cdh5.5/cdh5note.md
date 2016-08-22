### links

install help :

	http://blog.csdn.net/hua840812/article/details/27704025
	http://blog.csdn.net/kissmelove01/article/details/44680255
	http://blog.csdn.net/hualiu163/article/details/46659375
	http://blog.csdn.net/forest_boy/article/details/5636696
	http://itindex.net/detail/51928-cloudera-manager-cdh5

cdh5 tarball

	http://archive.cloudera.com/cdh5/repo-as-tarball/

rmp download page:

	http://archive-primary.cloudera.com/cm5/redhat/6/x86_64/cm/5/RPMS/x86_64/ # select stable version
	http://archive-primary.cloudera.com/cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/noarch/
	http://archive-primary.cloudera.com/cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/x86_64/

	if the download speed is low ,try the domain:
		http://archive.cloudera.com

- parcels

repl location :

	/opt/cloudera/parcel-repo

download parcels and sha1 file from

	http://archive.cloudera.com/cdh5/parcels/

then rename *.sha1 file to *.sha

	wget -r -np -nH http://archive-primary.cloudera.com/cm5/redhat/6/x86_64/cm/5/RPMS/x86_64/ && wget -r -np -nH http://archive-primary.cloudera.com/cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/noarch && wget -r -np -nH http://archive-primary.cloudera.com/cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/x86_64

	scp cm5/redhat/6/x86_64/cm/5/RPMS/x86_64/*.rpm cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/noarch/*.rpm cdh5/redhat/6/x86_64/cdh/5.4.3/RPMS/x86_64/*.rpm root@192.168.0.96:/var/http/pub/

### mysql

	yum install mysql-server

安装 mysql后的配置 ，查看 /var/log/mysqld.log，看是否有相关错误

```
create database amon DEFAULT CHARACTER SET utf8;
create database rman DEFAULT CHARACTER SET utf8;
create database metastore DEFAULT CHARACTER SET utf8;
create database sentry DEFAULT CHARACTER SET utf8;
create database nav DEFAULT CHARACTER SET utf8;
create database navms DEFAULT CHARACTER SET utf8;
create database ooziedata DEFAULT CHARACTER SET utf8;
create database monitordata DEFAULT CHARACTER SET utf8;

grant all on amon.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on rman.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on metastore.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on sentry.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on nav.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on navms.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on ooziedata.* TO 'root'@'master' IDENTIFIED BY 'root123';
grant all on monitordata.* TO 'root'@'master' IDENTIFIED BY 'root123';


grant all on amon.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on rman.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on metastore.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on sentry.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on nav.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on navms.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on ooziedata.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';
grant all on monitordata.* TO 'root'@'cdh-master' IDENTIFIED BY 'root123';

drop database amon;
drop database rman;
drop database metastore;
drop database sentry;
drop database nav;
drop database navms;
drop database ooziedata;
drop database monitordata;
drop database scm;
```

- mysql conf

```
[mysqld]
default-storage-engine = innodb
transaction-isolation = READ-COMMITTED
binlog_format = mixed

key_buffer = 16M
key_buffer_size = 32M
max_allowed_packet = 32M
thread_stack = 256K
thread_cache_size = 64
query_cache_limit = 8M
query_cache_size = 64M
query_cache_type = 1

log_bin=/var/lib/mysql/mysql_binary_log

max_connections = 550

read_buffer_size = 2M
read_rnd_buffer_size = 16M
sort_buffer_size = 8M
join_buffer_size = 8M

innodb_file_per_table = 1
innodb_flush_log_at_trx_commit  = 2
innodb_log_buffer_size = 64M
innodb_buffer_pool_size = 1G
innodb_thread_concurrency = 8
innodb_flush_method = O_DIRECT
innodb_log_file_size = 128M


[mysqld_safe]
log-error=/var/log/mysqld.log
pid-file=/var/run/mysqld/mysqld.pid
```

### nginx配置

/etc/nginx/conf.d/defautl.conf

```
	root	/var/http/pub;
	autoindex	on;
```

### centos repo conf

cdh5.repo配置 local /etc/yum.list/

```
[cdh5]
name=cdh5 repo
baseurl=http://cdh-master
gpgcheck=0
```
### jdk 安装配置

	yum search oracle
  yum install oracle-j2sdk1.7.x86_64

- install mysql connector

	cd $JAVA_HOME/jre/lib/ext
	wget http://central.maven.org/maven2/mysql/mysql-connector-java/5.1.18/mysql-connector-java-5.1.18.jar

### install cloudera-manager-server

- install

	yum install -y cloudera-manager-daemons cloudera-manager-server

- init

	/usr/share/cmf/schema/scm_prepare_database.sh mysql -uroot -p --scm-host localhost scm scm scm

- start

	service cloudera-scm-server start

### help info

clouder manager webui port: 7180

google 统计 代码：

	cd /usr/share/cmf/webapp/static/ext/google-analytics/scmx.js
	注释掉相关 请求代码

- like oozie

	https://azkaban.github.io/

- ntp

	vim /etc/ntp.conf

```
server 2.cn.pool.ntp.org
server 0.asia.pool.ntp.org
server 3.asia.pool.ntp.org
```

	service ntpd start
	chkconfig ntpd on




### network set:

DEVICE=eth0
HWADDR=08:00:27:22:FF:5C
TYPE=Ethernet
ONBOOT=yes
NM_CONTROLLED=yes
IPADDR=192.168.0.209
GATEWAY=192.168.0.1
NETMASK=255.255.255.0
BOOTPROTO=static

vim /etc/udev/rules.d/70-persistent-net.rules 修改 保持 mac地址一致

安装 新的cluster时 could not contact scm server at localhost:7182, giving u

sudo mv /usr/bin/host /usr/bin/host.bak

说明：
不明白cloudera的初衷，这里已经得到 Cloudera Manager Server的ip了，却还要把ip解析成主机名来连接
由于DNS反向解析没有配置好，根据Cloudera Manager Server 的ip解析主机名却得到了localhost，造成之后的连接错误
这里的解决方案是直接把/usr/bin/host删掉，这样Cloudera Manager就会直接使用 ip进行连接，就没有错了


### 本地Yum文件 创建

	cd /var/http/pub
	yum install createrepo
	createrepo .
	在nginx 中设置 此目录 可web读取
	


###cdh monitor connect refuse

	cdh service config up java memory size

### hdfs nfsgate way start error

	check stderr log :
		service rpcbind start
		chkconfig rpcbind on

### help commands

- ssh-copy-id -i ~/.ssh/id_pub. root@slave03
- hadoop classpath  see hadoop_conf_dir LOCATION
- change hostname
	issue command:
	hostname newhostname
	then type:
	bash
