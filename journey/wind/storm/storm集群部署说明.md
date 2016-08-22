1、安装linux suse11 OS，配置网络连接。
2、部署jdk
A.        将jdk-6u13-linux-x64.bin文件上传到/usr/local/目录下；
B.        打开命令行窗口，执行cd /usr/local;
C.        执行./ jdk-6u13-linux-x64.bin，输入yes，确认；
D.        将解压好的文件夹改名字，执行mv jdk1.6 jdk；
3、部署zookeeper
A.        将zookeeper-3.4.5.tar.gz文件上传到/usr/local/下；
B.        在命令行中，执行cd /usr/local;
C.        执行tar -zxvf zookeeper-3.4.5.tar.gz;
D.        将解压好的文件夹改名字，执行mv zookeeper-3.4.5 zk;
E.         修改配置文件，执行mv /usr/local/zk/conf/zoo_sample.cfg;
F.         执行vi /usr/local/zk/conf/zoo.cfg，复制下面文本粘贴保存退出；
server.1=10.100.4.32:2888:3888
server.2=10.100.6.255:2888:3888
server.3=10.100.4.239:2888:3888
G.        执行mkdir /tmp/zookeeper
H.        执行cd /tmp/zookeeper，vi myid，然后输入1，保存退出；
 
4、部署storm
A.                 将apache-storm-0.9.1-incubating.tar.gz文件上传到/usr/local/下；
B.        在命令行中，执行cd /usr/local;
C.        执行tar -zxvf apache-storm-0.9.1-incubating.tar.gz;
D.        将解压好的文件夹改名字，执行mv pache-storm-0.9.1-incubating storm;
E.         修改配置文件，执行vi /usr/local/storm/conf/storm.yaml，复制粘贴下面文本，然后保存退出；
storm.messaging.transport: "backtype.storm.messaging.netty.Context"
storm.messaging.netty.server_worker_threads: 1 
storm.messaging.netty.client_worker_threads: 1  
storm.messaging.netty.buffer_size: 5242880   
storm.messaging.netty.max_retries: 100 
storm.messaging.netty.max_wait_ms: 1000
storm.messaging.netty.min_wait_ms: 100 
storm.zookeeper.servers:
- "10.100.4.32"
storm.local.dir: "/user/local/storm/workdir"
nimbus.host: "10.100.4.32" 
supervisor.slots.ports:
    - 6700
    - 6701
    - 6702
    - 6703
drpc.servers:
     - "10.100.4.32"
5、配置环境变量，执行vi /etc/profile，将下面文本复制、粘贴、保存、退出；
export JAVA_HOME=/usr/local/jdk
export ZOOKEEPER_HOME=/usr/local/zk
export STORM_HOME=/usr/local/storm
export PATH=.:$JAVA_HOME/bin:$ZOOKEEPER_HOME/bin:$STORM_HOME/bin:$PATH