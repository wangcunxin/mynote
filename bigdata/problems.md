
1.同步时间
ntpdate 2.cn.pool.ntp.org

2.
hdfs archive -archiveName ymd.har -p src dest
har://namenode:8020/xxx

3.yum groupinstall -y 'development tools'

4.spark streaming没有缓存offset，一旦中断，数据不连续，升级不便。
因此，将offset写入zk，启动时读出，消费时，更新zk中的offset。