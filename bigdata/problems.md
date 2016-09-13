
1.同步时间
ntpdate 2.cn.pool.ntp.org

2.
hdfs archive -archiveName ymd.har -p src dest
har://namenode:8020/xxx

3.yum groupinstall -y 'development tools'

4.spark streaming没有缓存offset，一旦中断，数据不连续，升级不便。
因此，将offset写入zk，启动时读出，消费时，更新zk中的offset。

5.defunct process 删除僵尸进程
ps -ef|grep defunct
hdfs     115285 115279  0 01:00 ?        00:00:00 [rc-cinema.sh] <defunct>

kill -9 pid 无效
kill -9 115279<父进程ppid>

6.java.io.IOException: failed log splitting  不能正常启动hbase master
hdfs dfs -mv /hbase/WALs/dn4.gewara.cn,60020,1447899619647-splitting /tmp/hbase2/

7.hdfs 开启回收站服务
/user/用户名/.Trash/Current/文件 
core-site.xml
<property>  
<name>fs.trash.interval</name>  
<value>1440</value>  
</property>  

8.保留 HLogs 的最长时间
hbase.master.logcleaner.ttl 1

