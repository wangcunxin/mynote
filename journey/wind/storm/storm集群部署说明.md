1����װlinux suse11 OS�������������ӡ�
2������jdk
A.        ��jdk-6u13-linux-x64.bin�ļ��ϴ���/usr/local/Ŀ¼�£�
B.        �������д��ڣ�ִ��cd /usr/local;
C.        ִ��./ jdk-6u13-linux-x64.bin������yes��ȷ�ϣ�
D.        ����ѹ�õ��ļ��и����֣�ִ��mv jdk1.6 jdk��
3������zookeeper
A.        ��zookeeper-3.4.5.tar.gz�ļ��ϴ���/usr/local/�£�
B.        ���������У�ִ��cd /usr/local;
C.        ִ��tar -zxvf zookeeper-3.4.5.tar.gz;
D.        ����ѹ�õ��ļ��и����֣�ִ��mv zookeeper-3.4.5 zk;
E.         �޸������ļ���ִ��mv /usr/local/zk/conf/zoo_sample.cfg;
F.         ִ��vi /usr/local/zk/conf/zoo.cfg�����������ı�ճ�������˳���
server.1=10.100.4.32:2888:3888
server.2=10.100.6.255:2888:3888
server.3=10.100.4.239:2888:3888
G.        ִ��mkdir /tmp/zookeeper
H.        ִ��cd /tmp/zookeeper��vi myid��Ȼ������1�������˳���
 
4������storm
A.                 ��apache-storm-0.9.1-incubating.tar.gz�ļ��ϴ���/usr/local/�£�
B.        ���������У�ִ��cd /usr/local;
C.        ִ��tar -zxvf apache-storm-0.9.1-incubating.tar.gz;
D.        ����ѹ�õ��ļ��и����֣�ִ��mv pache-storm-0.9.1-incubating storm;
E.         �޸������ļ���ִ��vi /usr/local/storm/conf/storm.yaml������ճ�������ı���Ȼ�󱣴��˳���
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
5�����û���������ִ��vi /etc/profile���������ı����ơ�ճ�������桢�˳���
export JAVA_HOME=/usr/local/jdk
export ZOOKEEPER_HOME=/usr/local/zk
export STORM_HOME=/usr/local/storm
export PATH=.:$JAVA_HOME/bin:$ZOOKEEPER_HOME/bin:$STORM_HOME/bin:$PATH